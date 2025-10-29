package com.example.healthassistantbackend.repository;

import com.example.healthassistantbackend.entity.BodyMetricRecord;
import com.example.healthassistantbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyMetricRecordRepository extends JpaRepository<BodyMetricRecord, Long> {
    // 查找某个用户的最新一条记录
    Optional<BodyMetricRecord> findFirstByUserOrderByRecordDateDesc(User user);

    // 查找某个用户的所有记录，并按日期升序排列（便于图表绘制）
    List<BodyMetricRecord> findAllByUserOrderByRecordDateAsc(User user);
    void deleteByIdAndUser(Long id, User user);
}