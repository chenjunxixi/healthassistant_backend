package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.dto.ai.DeepSeekRequest;
import com.example.healthassistantbackend.dto.ai.Message; // 【重要】确保导入了您自己的Message类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List; // 【重要】导入 List

@Service
public class AiService {

    @Autowired
    @Qualifier("deepseekWebClient")
    private WebClient webClient;

    @Value("${ai.deepseek.model}")
    private String deepseekModel;

    /**
     * 【核心修改】方法签名现在接收一个完整的消息列表，而不仅仅是一个问题字符串。
     * @param messages 前端传递过来的完整对话历史
     * @return 返回一个字符串的响应流
     */
    public Flux<String> streamChat(List<Message> messages) {

        // 1. 【核心修改】直接使用传入的消息列表来构建发送给第三方API的请求体。
        //    不再需要在这里手动创建 new Message(...)。
        DeepSeekRequest request = new DeepSeekRequest(
                deepseekModel,
                messages, // <-- 使用前端传入的完整对话历史
                true      // 开启流式返回
        );

        // 2. WebClient 的调用逻辑保持不变，它已经写得很好了。
        return webClient.post()
                .uri("/chat/completions") // DeepSeek 的流式聊天端点
                .bodyValue(request)      // 设置请求体
                .retrieve()              // 发送请求并获取响应
                .bodyToFlux(String.class) // 将响应体作为字符串流返回
                .doOnError(error -> {
                    // 保留这个有用的错误日志
                    System.err.println("====== 调用DeepSeek API时发生严重错误 ======");
                    System.err.println("错误类型: " + error.getClass().getName());
                    System.err.println("错误信息: " + error.getMessage());
                    System.err.println("==========================================");
                });
    }
}