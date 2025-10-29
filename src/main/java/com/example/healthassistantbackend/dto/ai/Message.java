package com.example.healthassistantbackend.dto.ai;

public class Message {
    private String role;
    private String content;

    // 构造函数、Getter和Setter
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}