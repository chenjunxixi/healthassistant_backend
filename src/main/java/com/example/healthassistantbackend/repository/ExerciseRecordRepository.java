package com.example.healthassistantbackend.repository;

import com.example.healthassistantbackend.entity.ExerciseRecord;
import com.example.healthassistantbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {
    // 根据用户查找所有记录，并按日期降序排列
    List<ExerciseRecord> findByUserOrderByRecordDateDesc(User user);

    List<ExerciseRecord> findTop7ByUserOrderByRecordDateDesc(User user);
}
