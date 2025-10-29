package com.example.healthassistantbackend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BodyMetricRecordDto {
    private Long id;
    private Date recordDate;
    private float heightCm;
    private float weightKg;
    private Float bmi;
}
