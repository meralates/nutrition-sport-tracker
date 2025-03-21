package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.model.ExerciseType;
import com.example.nutritionsporttracker.service.WorkoutLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    @Autowired
    public WorkoutLogController(WorkoutLogService workoutLogService) {
        this.workoutLogService = workoutLogService;
    }

    @PostMapping
    public ResponseEntity<WorkoutLog> addWorkoutLog(@RequestBody WorkoutLog workoutLog) {
        return new ResponseEntity<>(workoutLogService.addWorkoutLog(workoutLog), HttpStatus.CREATED);
    }

    // 📌 Kullanıcının tüm egzersiz geçmişini getir
    @GetMapping("/history")
    public ResponseEntity<List<WorkoutLog>> getWorkoutHistory(@RequestParam Long userId) {
        List<WorkoutLog> workoutLogs = workoutLogService.getWorkoutLogsByUserId(userId);
        return workoutLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(workoutLogs);
    }

    // 📌 Kullanıcının belirli bir egzersiz türüne göre geçmişini getir
    @GetMapping("/history/filter")
    public ResponseEntity<List<WorkoutLog>> getWorkoutHistoryByExerciseType(
            @RequestParam Long userId, @RequestParam ExerciseType exerciseType) {
        List<WorkoutLog> workoutLogs = workoutLogService.getWorkoutLogsByUserIdAndExerciseType(userId, exerciseType);
        return workoutLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(workoutLogs);
    }
}
