package com.example.healthassistantbackend.dto;

public class LoginResponse {
    private int code;
    private String message;
    private UserData data;

    // 全参构造函数，用于创建响应对象
    public LoginResponse(int code, String message, UserData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // --- Getters ---
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public UserData getData() { return data; }

    /**
     * 内部静态类，用于承载 token 和 username
     */
    public static class UserData {
        private String token;
        private String username;

        // 全参构造函数
        public UserData(String token, String username) {
            this.token = token;
            this.username = username;
        }

        // --- Getters ---
        public String getToken() { return token; }
        public String getUsername() { return username; }
    }
}