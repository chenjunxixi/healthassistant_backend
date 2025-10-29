package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.entity.CalorieRecord;
import com.example.healthassistantbackend.service.CalorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calories")
public class CalorieController {

    @Autowired
    private CalorieService calorieService;

    // 创建一条新的热量记录
    @PostMapping
    public ResponseEntity<CalorieRecord> saveCalorieRecord(
            @RequestBody CalorieRecord calorieRecord,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalorieRecord savedRecord = calorieService.saveCalorieRecord(calorieRecord, userDetails.getUsername());
        return ResponseEntity.ok(savedRecord);
    }

    // 获取当前登录用户的所有热量记录
    @GetMapping
    public ResponseEntity<List<CalorieRecord>> getUserCalories(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<CalorieRecord> records = calorieService.getRecordsForUser(userDetails.getUsername());
        return ResponseEntity.ok(records);
    }

    // 【新增】修改一条热量记录
    @PutMapping("/{id}")
    public ResponseEntity<CalorieRecord> updateCalorieRecord(
            @PathVariable Long id, // 从URL路径中获取记录的ID
            @RequestBody CalorieRecord calorieRecordDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalorieRecord updatedRecord = calorieService.updateCalorieRecord(id, calorieRecordDetails, userDetails.getUsername());
        return ResponseEntity.ok(updatedRecord);
    }

    // 【新增】删除一条热量记录
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalorieRecord(
            @PathVariable Long id, // 从URL路径中获取记录的ID
            @AuthenticationPrincipal UserDetails userDetails) {
        calorieService.deleteCalorieRecord(id, userDetails.getUsername());
        // 删除成功，返回 204 No Content 状态码
        return ResponseEntity.noContent().build();
    }
}