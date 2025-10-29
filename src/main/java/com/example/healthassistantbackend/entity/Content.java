package com.example.healthassistantbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "content_library")
@Data
@NoArgsConstructor
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1024) // 允许更长的描述
    private String description;

    @Column(nullable = false)
    private String type; // "article" 或 "video"

    private String coverImageUrl;
    private String contentUrl;
    private String tags;

    // 构造函数，方便我们添加示例数据
    public Content(String title, String description, String type, String coverImageUrl, String contentUrl, String tags) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.coverImageUrl = coverImageUrl;
        this.contentUrl = contentUrl;
        this.tags = tags;
    }
}
