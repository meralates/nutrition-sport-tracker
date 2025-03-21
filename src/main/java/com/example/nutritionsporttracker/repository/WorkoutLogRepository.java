package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.model.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findByUserId(Long userId);

    // 📌 Kullanıcının belirli bir egzersiz türüne göre geçmişini çekme
    List<WorkoutLog> findByUserIdAndExerciseType(Long userId, ExerciseType exerciseType);
}
