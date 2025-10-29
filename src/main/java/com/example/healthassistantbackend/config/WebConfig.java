package com.example.healthassistantbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 这个方法用于配置静态资源的处理

        // "file:" 协议用于指定绝对文件路径
        // 请确保这里的路径和你 UserService 中设置的路径一致
        String externalUploadPath = "file:D:/health_assistant/uploads/avatars/";

        // addResourceHandler("/images/avatars/**")
        // 定义了URL的请求模式。当浏览器或App请求的URL以 "/images/avatars/" 开头时...

        // addResourceLocations(externalUploadPath)
        // ...Spring Boot就会去 externalUploadPath 定义的物理路径下查找对应的文件。
        registry.addResourceHandler("/images/avatars/**")
                .addResourceLocations(externalUploadPath);
    }
}
