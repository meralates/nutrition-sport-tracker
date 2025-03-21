package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.model.ExerciseType;
import com.example.nutritionsporttracker.repository.WorkoutLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;

    @Autowired
    public WorkoutLogService(WorkoutLogRepository workoutLogRepository) {
        this.workoutLogRepository = workoutLogRepository;
    }

    public WorkoutLog addWorkoutLog(WorkoutLog workoutLog) {
        return workoutLogRepository.save(workoutLog);
    }

    public List<WorkoutLog> getWorkoutLogsByUserId(Long userId) {
        return workoutLogRepository.findByUserId(userId);
    }

    // 📌 Kullanıcının belirli bir egzersiz türüne göre geçmişini çekme
    public List<WorkoutLog> getWorkoutLogsByUserIdAndExerciseType(Long userId, ExerciseType exerciseType) {
        return workoutLogRepository.findByUserIdAndExerciseType(userId, exerciseType);
    }
}
