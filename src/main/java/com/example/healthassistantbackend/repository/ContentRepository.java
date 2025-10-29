package com.example.healthassistantbackend.repository;

import com.example.healthassistantbackend.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    boolean existsByContentUrl(String url);
}
