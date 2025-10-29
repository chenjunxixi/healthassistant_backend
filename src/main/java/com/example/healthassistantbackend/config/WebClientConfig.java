package com.example.healthassistantbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.deepseek.base-url}")
    private String deepseekBaseUrl;

    @Value("${ai.deepseek.api-key}")
    private String deepseekApiKey;

    @Bean("deepseekWebClient")
    public WebClient deepseekWebClient() {
        if (deepseekApiKey == null || deepseekApiKey.isBlank() || deepseekApiKey.equals("sk-xxxxxxxx")) {
            throw new IllegalArgumentException("DeepSeek API Key (ai.deepseek.api-key) 未在 application.properties 中正确配置");
        }
        return WebClient.builder()
                .baseUrl(deepseekBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + deepseekApiKey)
                .build();
    }
}