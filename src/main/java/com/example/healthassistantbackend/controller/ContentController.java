package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.dto.ContentDto;
import com.example.healthassistantbackend.entity.Content;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    // 获取所有内容，并标记出当前用户收藏了哪些
    @GetMapping
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContentDto>> getAllContent(@AuthenticationPrincipal UserDetails userDetails) {
        List<Content> allContent = contentService.getAllContent();
        Set<Content> favorites = contentService.getFavorites(userDetails.getUsername());

        List<ContentDto> dtoList = allContent.stream().map(content -> {
            boolean isFavorited = favorites.stream().anyMatch(fav -> fav.getId().equals(content.getId()));
            return new ContentDto(content, isFavorited);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // 添加收藏
    @PostMapping("/{contentId}/favorite")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addFavorite(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails userDetails) {
        contentService.addFavorite(contentId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/favorites")
    public ResponseEntity<List<ContentDto>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        Set<Content> favorites = contentService.getFavorites(userDetails.getUsername());

        // 将收藏的Content实体列表，转换为ContentDto列表
        // 在这个过程中，我们明确地将 isFavorited 设为 true
        List<ContentDto> dtoList = favorites.stream()
                .map(content -> new ContentDto(content, true)) // <-- 关键在这里
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
    // 移除收藏
    @DeleteMapping("/{contentId}/favorite")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails userDetails) {
        contentService.removeFavorite(contentId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/discover")
    public ResponseEntity<?> discoverNewContent(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<Content> newContent = contentService.discoverAndSaveNewContent(userDetails.getUsername());
        if (newContent.isPresent()) {
            return ResponseEntity.ok(newContent.get());
        } else {
            return ResponseEntity.ok().body("没有发现更多新文章。");
        }
    }
}

