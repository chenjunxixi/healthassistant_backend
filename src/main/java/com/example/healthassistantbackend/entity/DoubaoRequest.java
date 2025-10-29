package com.example.healthassistantbackend.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DoubaoRequest {
    // 【修改】将模型ID更新为和您Python代码中一致的正确ID
    private String model = "doubao-1.5-vision-pro-250328";
    private List<Message> messages;
    private Boolean stream = false;

    public DoubaoRequest(String model, String text, boolean stream) {
        this.model = model;
        this.stream = stream;
        ContentPart textPart = new ContentPart("text", text);
        this.messages = Collections.singletonList(new Message("user", Collections.singletonList(textPart)));
    }

    public DoubaoRequest(String text, String base64Image) {
        ContentPart textPart = new ContentPart("text", text);
        ContentPart imagePart = new ContentPart("image_url", new ImageUrl("data:image/jpeg;base64," + base64Image));
        this.messages = Collections.singletonList(new Message("user", Arrays.asList(textPart, imagePart)));
    }

    // --- 内部类 (保持不变) ---
    public static class Message {
        private String role;
        private List<ContentPart> content;
        public Message(String role, List<ContentPart> content) {
            this.role = role;
            this.content = content;
        }
    }

    public static class ContentPart {
        private String type;
        private String text;
        private ImageUrl image_url;
        public ContentPart(String type, String text) {
            this.type = type;
            this.text = text;
        }
        public ContentPart(String type, ImageUrl imageUrl) {
            this.type = type;
            this.image_url = imageUrl;
        }
    }

    public static class ImageUrl {
        private String url;
        public ImageUrl(String url) {
            this.url = url;
        }
    }
}