package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.dto.ai.ChatRequest;
import com.example.healthassistantbackend.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai") // 类级别的基础路径
public class AiController {

    @Autowired
    private AiService aiService;

    /**
     * 处理AI聊天请求的SSE (Server-Sent Events) 端点
     * 1. 使用 @PostMapping 来匹配前端的POST请求
     * 2. 路径设置为 "/chat/sse"，与前端请求的 /api/ai/chat/sse 完全匹配
     * 3. 使用 @RequestBody 接收前端在请求体中发送的JSON数据，并自动映射到 ChatRequest 对象
     */
    @PostMapping("/chat/sse")
    public SseEmitter chatSSE(@RequestBody ChatRequest chatRequest) {
        // 创建一个SseEmitter实例，用于向客户端发送事件。设置60秒超时。
        SseEmitter emitter = new SseEmitter(60_000L);

        // 调用Service层的方法，将整个消息列表传递过去，获取响应流
        Flux<String> stream = aiService.streamChat(chatRequest.getMessages());

        // 订阅数据流，并在接收到数据、错误或完成信号时，通过emitter转发给前端
        stream.subscribe(
                data -> { // onNext: 当接收到新的数据块时
                    try {
                        // 将数据块包装成一个SSE事件发送出去
                        emitter.send(SseEmitter.event().data(data));
                    } catch (Exception e) {
                        // 如果在发送时发生异常（如客户端断开连接），则终止此次会话
                        emitter.completeWithError(e);
                    }
                },
                emitter::completeWithError, // onError: 当数据流发生错误时，将错误信息通知给客户端
                emitter::complete // onComplete: 当数据流正常结束时，关闭连接
        );

        // 返回emitter对象给Spring MVC，建立与客户端的长连接
        return emitter;
    }
}