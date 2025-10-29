package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.entity.ExerciseRecord;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.repository.ExerciseRecordRepository;
import com.example.healthassistantbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRecordRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("错误：找不到用户！"));
    }

    public ExerciseRecord saveExerciseRecord(ExerciseRecord exerciseRecord, String username) {
        User user = getUserByUsername(username);
        exerciseRecord.setUser(user);
        // 如果前端没传日期，则使用服务器当前时间
        if (exerciseRecord.getRecordDate() == null) {
            exerciseRecord.setRecordDate(new Date());
        }
        return exerciseRepository.save(exerciseRecord);
    }

    public List<ExerciseRecord> getRecordsForUser(String username) {
        User user = getUserByUsername(username);
        return exerciseRepository.findByUserOrderByRecordDateDesc(user);
    }

    // 【新增】修改记录的业务逻辑
    public ExerciseRecord updateExerciseRecord(Long id, ExerciseRecord recordDetails, String username) {
        User user = getUserByUsername(username);
        ExerciseRecord record = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：找不到ID为 " + id + " 的记录"));

        if (!Objects.equals(record.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("无权修改此记录");
        }

        record.setType(recordDetails.getType());
        record.setDurationMinutes(recordDetails.getDurationMinutes());
        record.setCaloriesBurned(recordDetails.getCaloriesBurned());
        record.setRecordDate(recordDetails.getRecordDate());

        return exerciseRepository.save(record);
    }

    // 【新增】删除记录的业务逻辑
    public void deleteExerciseRecord(Long id, String username) {
        User user = getUserByUsername(username);
        ExerciseRecord record = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：找不到ID为 " + id + " 的记录"));

        if (!Objects.equals(record.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("无权删除此记录");
        }
        exerciseRepository.delete(record);
    }
}