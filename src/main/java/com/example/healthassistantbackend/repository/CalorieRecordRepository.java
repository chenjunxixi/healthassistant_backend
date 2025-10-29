package com.example.healthassistantbackend.repository;

import com.example.healthassistantbackend.entity.CalorieRecord;
import com.example.healthassistantbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalorieRecordRepository extends JpaRepository<CalorieRecord, Long> {
    // 根据用户查找所有记录，并按日期降序排列
    List<CalorieRecord> findByUserOrderByRecordDateDesc(User user);
    List<CalorieRecord> findTop7ByUserOrderByRecordDateDesc(User user);
}