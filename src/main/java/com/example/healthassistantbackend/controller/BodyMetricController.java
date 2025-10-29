package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.dto.BodyMetricRecordDto; // 导入DTO
import com.example.healthassistantbackend.service.BodyMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodymetrics")
public class BodyMetricController {

    @Autowired
    private BodyMetricService bodyMetricService;

    @PostMapping
    public ResponseEntity<BodyMetricRecordDto> saveBodyMetric(
            @RequestBody BodyMetricRecordDto bodyMetricRecordDto, // 使用DTO
            @AuthenticationPrincipal UserDetails userDetails) {
        BodyMetricRecordDto savedRecord = bodyMetricService.saveBodyMetricRecord(bodyMetricRecordDto, userDetails.getUsername());
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/latest")
    public ResponseEntity<BodyMetricRecordDto> getLatestBodyMetric( // 返回DTO
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return bodyMetricService.getLatestRecordForUser(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history")
    public ResponseEntity<List<BodyMetricRecordDto>> getBodyMetricHistory( // 返回DTO列表
                                                                           @AuthenticationPrincipal UserDetails userDetails) {
        List<BodyMetricRecordDto> records = bodyMetricService.getAllRecordsForUser(userDetails.getUsername());
        return ResponseEntity.ok(records);
    }

    // 【新增】删除接口
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodyMetric(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        bodyMetricService.deleteRecord(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodyMetricRecordDto> updateBodyMetric(
            @PathVariable Long id,
            @RequestBody BodyMetricRecordDto bodyMetricRecordDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        BodyMetricRecordDto updatedRecord = bodyMetricService.updateRecord(id, bodyMetricRecordDto, userDetails.getUsername());
        return ResponseEntity.ok(updatedRecord);
    }
}