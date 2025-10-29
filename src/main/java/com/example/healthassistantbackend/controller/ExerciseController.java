package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.entity.ExerciseRecord;
import com.example.healthassistantbackend.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseRecord> saveExerciseRecord(
            @RequestBody ExerciseRecord exerciseRecord,
            @AuthenticationPrincipal UserDetails userDetails) {

        ExerciseRecord savedRecord = exerciseService.saveExerciseRecord(exerciseRecord, userDetails.getUsername());
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseRecord>> getUserExercises(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<ExerciseRecord> records = exerciseService.getRecordsForUser(userDetails.getUsername());
        return ResponseEntity.ok(records);
    }

    // 【新增】修改一条锻炼记录
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseRecord> updateExerciseRecord(
            @PathVariable Long id,
            @RequestBody ExerciseRecord recordDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        ExerciseRecord updatedRecord = exerciseService.updateExerciseRecord(id, recordDetails, userDetails.getUsername());
        return ResponseEntity.ok(updatedRecord);
    }

    // 【新增】删除一条锻炼记录
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExerciseRecord(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        exerciseService.deleteExerciseRecord(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}