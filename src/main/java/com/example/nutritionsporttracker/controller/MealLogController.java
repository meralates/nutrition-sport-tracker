package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.MealLogRequest;
import com.example.nutritionsporttracker.dto.MealLogResponse;
import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.MealTimeType;
import com.example.nutritionsporttracker.service.MealLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
public class MealLogController {

    private final MealLogService mealLogService;

    public MealLogController(MealLogService mealLogService) {
        this.mealLogService = mealLogService;
    }

    @PostMapping("/meal-logs")
    public ResponseEntity<MealLogResponse> createMealLog(@RequestBody MealLogRequest request) {
        return ResponseEntity.ok(mealLogService.addMealLog(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<MealLog>> getMealHistory(@RequestParam Long userId) {
        List<MealLog> mealLogs = mealLogService.getMealLogsByUserId(userId);
        return mealLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(mealLogs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealLog>> getMealLogsByUserId(@PathVariable Long userId) {
        List<MealLog> mealLogs = mealLogService.getMealLogsByUserId(userId);
        return mealLogs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(mealLogs);
    }

    @GetMapping("/history/filter")
    public ResponseEntity<List<MealLog>> getMealHistoryByMealTime(
            @RequestParam Long userId, @RequestParam MealTimeType mealTime) {
        List<MealLog> mealLogs = mealLogService.getMealLogsByUserIdAndMealTime(userId, mealTime);
        return mealLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(mealLogs);
    }

    @GetMapping("/history/daily")
    public ResponseEntity<List<MealLog>> getMealLogsByDate(
            @RequestParam Long userId,
            @RequestParam String date // YYYY-MM-DD formatında
    ) {
        LocalDate localDate = LocalDate.parse(date);
        List<MealLog> mealLogs = mealLogService.getMealLogsByUserIdAndDate(userId, localDate);
        return mealLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(mealLogs);
    }
}
