package com.example.healthassistantbackend.dto;

import com.example.healthassistantbackend.entity.Content;
import lombok.Data;

@Data
public class ContentDto {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String coverImageUrl;
    private String contentUrl;
    private String tags;
    private boolean isFavorited; // 包含了用户是否收藏的状态

    public ContentDto(Content content, boolean isFavorited) {
        this.id = content.getId();
        this.title = content.getTitle();
        this.description = content.getDescription();
        this.type = content.getType();
        this.coverImageUrl = content.getCoverImageUrl();
        this.contentUrl = content.getContentUrl();
        this.tags = content.getTags();
        this.isFavorited = isFavorited;
    }
}
