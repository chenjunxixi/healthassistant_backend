package com.example.healthassistantbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "body_metric_records")
@Data
public class BodyMetricRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date recordDate;

    @Column(nullable = false)
    private float heightCm;

    @Column(nullable = false)
    private float weightKg;

    // 【新增】增加BMI字段
    private Float bmi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 【新增】在保存前自动计算BMI
    @PrePersist
    @PreUpdate
    public void calculateBmi() {
        if (heightCm > 0 && weightKg > 0) {
            float heightInMeters = heightCm / 100.0f;
            this.bmi = weightKg / (heightInMeters * heightInMeters);
        }
    }
}