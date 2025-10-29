package com.example.healthassistantbackend.entity;

// 【移除】com.fasterxml.jackson.annotation.JsonFormat 的导入
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "calorie_records")
@Data
public class CalorieRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 【【【 移除这里的 @JsonFormat 注解 】】】
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date recordDate;

    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}