package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.dto.BodyMetricRecordDto;
import com.example.healthassistantbackend.entity.BodyMetricRecord;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.repository.BodyMetricRecordRepository;
import com.example.healthassistantbackend.repository.UserRepository;
import jakarta.transaction.Transactional; // 导入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BodyMetricService {

    @Autowired private BodyMetricRecordRepository bodyMetricRepository;
    @Autowired private UserRepository userRepository;

    // --- DTO与Entity的转换辅助方法 ---
    private BodyMetricRecord convertToEntity(BodyMetricRecordDto dto, User user) {
        BodyMetricRecord entity = new BodyMetricRecord();
        entity.setHeightCm(dto.getHeightCm());
        entity.setWeightKg(dto.getWeightKg());
        if(dto.getRecordDate() != null) {
            entity.setRecordDate(dto.getRecordDate());
        } else {
            entity.setRecordDate(new Date());
        }
        entity.setUser(user);
        return entity;
    }

    private BodyMetricRecordDto convertToDto(BodyMetricRecord entity) {
        BodyMetricRecordDto dto = new BodyMetricRecordDto();
        dto.setId(entity.getId());
        dto.setRecordDate(entity.getRecordDate());
        dto.setHeightCm(entity.getHeightCm());
        dto.setWeightKg(entity.getWeightKg());
        dto.setBmi(entity.getBmi());
        return dto;
    }
    // --- 转换方法结束 ---

    public BodyMetricRecordDto saveBodyMetricRecord(BodyMetricRecordDto dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        BodyMetricRecord entity = convertToEntity(dto, user);
        BodyMetricRecord savedEntity = bodyMetricRepository.save(entity);
        return convertToDto(savedEntity);
    }

    public Optional<BodyMetricRecordDto> getLatestRecordForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return bodyMetricRepository.findFirstByUserOrderByRecordDateDesc(user)
                .map(this::convertToDto); // 将查找到的Entity转换为DTO
    }

    public List<BodyMetricRecordDto> getAllRecordsForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return bodyMetricRepository.findAllByUserOrderByRecordDateAsc(user)
                .stream()
                .map(this::convertToDto) // 将Entity列表转换为DTO列表
                .collect(Collectors.toList());
    }

    // 【新增】删除记录的业务逻辑
    @Transactional
    public BodyMetricRecordDto updateRecord(Long recordId, BodyMetricRecordDto dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        BodyMetricRecord record = bodyMetricRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found: " + recordId));

        // 验证这条记录是否属于当前用户
        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("无权修改此记录");
        }

        record.setHeightCm(dto.getHeightCm());
        record.setWeightKg(dto.getWeightKg());
        record.setRecordDate(dto.getRecordDate()); // 允许修改记录时间

        BodyMetricRecord updatedRecord = bodyMetricRepository.save(record);
        return convertToDto(updatedRecord);
    }

    // 【修改】之前的删除方法需要传入User对象，我们简化一下
    @Transactional
    public void deleteRecord(Long recordId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        BodyMetricRecord record = bodyMetricRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found: " + recordId));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("无权删除此记录");
        }

        bodyMetricRepository.deleteById(recordId);
    }
}