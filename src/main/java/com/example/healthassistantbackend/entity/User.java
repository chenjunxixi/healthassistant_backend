package com.example.healthassistantbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "avatar_url", length = 255, nullable = true)
    private String avatarUrl;

    private String nickname;
    private String gender;
    private Integer age; // 使用Integer对象类型，允许为空
    private Float heightCm; // 保存最新的身高
    private Float weightKg; // 保存最新的体重

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorites", // 中间表的名字
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    @JsonIgnore // 返回用户信息时不包含收藏列表，避免循环
    @ToString.Exclude // 避免Lombok的ToString方法导致循环
    @EqualsAndHashCode.Exclude // 避免Equals和HashCode方法导致循环
    private Set<Content> favoriteContents = new HashSet<>();
}