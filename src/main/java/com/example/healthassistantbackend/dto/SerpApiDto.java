package com.example.healthassistantbackend.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

// 用于 SerpAPI 的响应 DTO
public class SerpApiDto {
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("organic_results")
        private List<OrganicResult> organicResults;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrganicResult {
        private String title;
        private String link;
        private String snippet;
    }
}
