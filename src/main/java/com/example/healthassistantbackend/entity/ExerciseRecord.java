package com.example.healthassistantbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "exercise_records")
@Data
public class ExerciseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date recordDate;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private String type;

    // 【新增】与前端模型保持一致，添加消耗热量字段
    @Column(name = "calories_burned") // 数据库列名
    private float caloriesBurned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}