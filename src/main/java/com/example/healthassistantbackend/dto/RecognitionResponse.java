package com.example.healthassistantbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// 使用Lombok简化代码，如果项目中没有，请手动添加Getters/Setters
@Data
@NoArgsConstructor
public class RecognitionResponse {

    private FoodInfoDTO foodInfo;
    private String uploadedImageUrl;
    private String success;
    private String error;

    // 内部类，用于封装食物信息
    @Data
    @NoArgsConstructor
    public static class FoodInfoDTO {
        private String name;
        private Double calories;
        private String description;
        private Double protein;
        private Double carbs;
        private Double fat;
        private Double fiber;
    }
}
