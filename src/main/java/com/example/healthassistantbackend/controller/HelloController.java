package com.example.healthassistantbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "你好，IDEA上的Spring Boot! 您的健康助手后端项目已成功启动！";
    }
}
