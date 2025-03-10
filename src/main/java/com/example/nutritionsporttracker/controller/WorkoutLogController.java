package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.service.WorkoutLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-log")
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    @Autowired
    public WorkoutLogController(WorkoutLogService workoutLogService) {
        this.workoutLogService = workoutLogService;
    }

    // Kullanıcı için egzersiz kaydı ekleme
    @PostMapping
    public ResponseEntity<WorkoutLog> addWorkoutLog(@RequestBody WorkoutLog workoutLog) {
        return new ResponseEntity<>(workoutLogService.addWorkoutLog(workoutLog), HttpStatus.CREATED);
    }

    // Kullanıcı ID'sine göre egzersiz geçmişini listeleme
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutLog>> getWorkoutLogsByUserId(@PathVariable Long userId) {
        List<WorkoutLog> workoutLogs = workoutLogService.getWorkoutLogsByUserId(userId);
        if (workoutLogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(workoutLogs);
    }
}
