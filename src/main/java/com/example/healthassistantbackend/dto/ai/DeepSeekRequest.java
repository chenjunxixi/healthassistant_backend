package com.example.healthassistantbackend.dto.ai;

import java.util.List;

public class DeepSeekRequest {
    private String model;
    private List<Message> messages;
    private boolean stream;

    // 构造函数、Getter和Setter
    public DeepSeekRequest(String model, List<Message> messages, boolean stream) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    public boolean isStream() { return stream; }
    public void setStream(boolean stream) { this.stream = stream; }
}
