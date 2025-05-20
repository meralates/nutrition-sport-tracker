package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.ExerciseRequest;
import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/log")
    public ResponseEntity<List<WorkoutLog>> logExercise(@RequestBody ExerciseRequest request) {
        List<WorkoutLog> logs = exerciseService.processExercise(request.getUserId(), request.getQuery());
        return ResponseEntity.ok(logs);
    }
}
