package com.example.healthassistantbackend.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

// 用于 Deepseek API 的请求和响应 DTO
public class DeepseekDto {
    @Data
    public static class Request {
        private String model;
        private List<Message> messages;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private List<Choice> choices;
        public String getResponseText() {
            if (choices != null && !choices.isEmpty()) {
                return choices.get(0).getMessage().getContent();
            }
            return null;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Message message;
    }
}
