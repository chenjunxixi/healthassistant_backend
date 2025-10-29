package com.example.healthassistantbackend.dto.ai; // 或者 com.example.healthassistantbackend.dto.ai

import com.example.healthassistantbackend.dto.ai.Message; // 【重要】导入您自己的Message类
import lombok.Data;
import java.util.List;

@Data
public class ChatRequest {
    private List<Message> messages;
}