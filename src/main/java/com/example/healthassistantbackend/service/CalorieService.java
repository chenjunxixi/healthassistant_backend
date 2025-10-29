package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.entity.CalorieRecord;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.repository.CalorieRecordRepository;
import com.example.healthassistantbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CalorieService {

    @Autowired
    private CalorieRecordRepository calorieRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("错误：找不到用户！"));
    }

    public CalorieRecord saveCalorieRecord(CalorieRecord calorieRecord, String username) {
        User user = getUserByUsername(username);
        calorieRecord.setUser(user);

        // 如果前端没有传递日期，则设置为当前服务器时间
        if (calorieRecord.getRecordDate() == null) {
            calorieRecord.setRecordDate(new Date());
        }

        return calorieRepository.save(calorieRecord);
    }

    public List<CalorieRecord> getRecordsForUser(String username) {
        User user = getUserByUsername(username);
        return calorieRepository.findByUserOrderByRecordDateDesc(user);
    }

    // 【新增】修改记录的业务逻辑
    public CalorieRecord updateCalorieRecord(Long id, CalorieRecord recordDetails, String username) {
        User user = getUserByUsername(username);
        CalorieRecord record = calorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：找不到ID为 " + id + " 的记录"));

        // 安全校验：确保这条记录属于当前操作的用户
        if (!Objects.equals(record.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("无权修改此记录");
        }

        record.setFoodName(recordDetails.getFoodName());
        record.setCalories(recordDetails.getCalories());
        // 允许前端传递修改后的时间
        if (recordDetails.getRecordDate() != null) {
            record.setRecordDate(recordDetails.getRecordDate());
        }

        return calorieRepository.save(record);
    }

    // 【新增】删除记录的业务逻辑
    public void deleteCalorieRecord(Long id, String username) {
        User user = getUserByUsername(username);
        CalorieRecord record = calorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：找不到ID为 " + id + " 的记录"));

        // 安全校验：确保这条记录属于当前操作的用户
        if (!Objects.equals(record.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("无权删除此记录");
        }

        calorieRepository.delete(record);
    }
}