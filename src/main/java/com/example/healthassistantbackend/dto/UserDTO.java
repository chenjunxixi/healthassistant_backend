package com.example.healthassistantbackend.dto;

import lombok.Data;

/**
 * 用于前端和后端之间传输用户信息的 数据传输对象 (DTO)
 */
@Data // 使用Lombok简化代码，自动生成Getters, Setters, toString等方法
public class UserDTO {

    private String username;
    private String avatarUrl;

    // 【新增】与个人信息页UI对应的字段
    private String nickname;
    private String gender;
    private Integer age;
    private Float heightCm;
    private Float weightKg;

}