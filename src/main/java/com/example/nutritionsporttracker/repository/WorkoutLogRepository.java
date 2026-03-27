package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    // DailySummaryService için gerekli:
    List<WorkoutLog> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
