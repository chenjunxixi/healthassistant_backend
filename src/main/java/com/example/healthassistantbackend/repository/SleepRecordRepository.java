package com.example.healthassistantbackend.repository;

import com.example.healthassistantbackend.entity.SleepRecord;
import com.example.healthassistantbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {
    // 根据用户查找所有记录，并按日期降序排列
    List<SleepRecord> findByUserOrderByRecordDateDesc(User user);

    List<SleepRecord> findTop7ByUserOrderByRecordDateDesc(User user);
}
