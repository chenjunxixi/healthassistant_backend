package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.entity.SleepRecord;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.repository.SleepRecordRepository;
import com.example.healthassistantbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SleepService {

    @Autowired
    private SleepRecordRepository sleepRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("错误：找不到用户！"));
    }

    public SleepRecord saveSleepRecord(SleepRecord sleepRecord, String username) {
        User user = getUserByUsername(username);
        sleepRecord.setUser(user);
        if (sleepRecord.getRecordDate() == null) {
            sleepRecord.setRecordDate(new Date());
        }
        return sleepRepository.save(sleepRecord);
    }

    public List<SleepRecord> getRecordsForUser(String username) {
        User user = getUserByUsername(username);
        return sleepRepository.findByUserOrderByRecordDateDesc(user);
    }

    // 【新增】修改记录的业务逻辑
    public SleepRecord updateSleepRecord(Long id, SleepRecord recordDetails, String username) {
        User user = getUserByUsername(username);
        SleepRecord record = sleepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：找不到ID为 " + id + " 的记录"));

        if (!Objects.equals(record.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("无权修改此记录");
        }

        record.setDurationHours(recordDetails.getDurationHours());
        record.setQuality(recordDetails.getQuality());
        record.setRecordDate(recordDetails.getRecordDate());

        return sleepRepository.save(record);
    }

    // 【新增】删除记录的业务逻辑
    public void deleteSleepRecord(Long id, String username) {
        User user = getUserByUsername(username);
        SleepRecord record = sleepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：找不到ID为 " + id + " 的记录"));

        if (!Objects.equals(record.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("无权删除此记录");
        }
        sleepRepository.delete(record);
    }
}