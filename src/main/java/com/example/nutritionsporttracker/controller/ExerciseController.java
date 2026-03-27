package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.ExerciseRequest;
import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.security.CustomUserDetails;
import com.example.nutritionsporttracker.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/log")
    public ResponseEntity<WorkoutLog> logExercise(
            @AuthenticationPrincipal CustomUserDetails me,
            @Valid @RequestBody ExerciseRequest request
    ) {
        WorkoutLog log = exerciseService.logExercise(
                me.getId(),
                request.getExerciseType(),
                request.getExerciseName(),
                request.getDurationMinutes()
        );
        return ResponseEntity.ok(log);
    }

    @GetMapping("/me")
    public ResponseEntity<List<WorkoutLog>> getMyLogs(@AuthenticationPrincipal CustomUserDetails me) {
        return ResponseEntity.ok(exerciseService.getLogsByUser(me.getId()));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable Long logId,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        exerciseService.deleteExercise(logId, me.getId());
        return ResponseEntity.noContent().build();
    }
}