package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.entity.SleepRecord;
import com.example.healthassistantbackend.service.SleepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sleep")
public class SleepController {

    @Autowired
    private SleepService sleepService;

    @PostMapping
    public ResponseEntity<SleepRecord> saveSleepRecord(
            @RequestBody SleepRecord sleepRecord,
            @AuthenticationPrincipal UserDetails userDetails) {
        SleepRecord savedRecord = sleepService.saveSleepRecord(sleepRecord, userDetails.getUsername());
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping
    public ResponseEntity<List<SleepRecord>> getUserSleepRecords(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<SleepRecord> records = sleepService.getRecordsForUser(userDetails.getUsername());
        return ResponseEntity.ok(records);
    }

    // 【新增】修改一条睡眠记录
    @PutMapping("/{id}")
    public ResponseEntity<SleepRecord> updateSleepRecord(
            @PathVariable Long id,
            @RequestBody SleepRecord recordDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        SleepRecord updatedRecord = sleepService.updateSleepRecord(id, recordDetails, userDetails.getUsername());
        return ResponseEntity.ok(updatedRecord);
    }

    // 【新增】删除一条睡眠记录
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSleepRecord(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        sleepService.deleteSleepRecord(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}