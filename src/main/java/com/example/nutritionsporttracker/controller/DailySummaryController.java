package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.DailyNutritionSummary;
import com.example.nutritionsporttracker.service.MealLogService;
import com.example.nutritionsporttracker.service.WaterIntakeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/summary")
public class DailySummaryController {

    private final MealLogService mealLogService;
    private final WaterIntakeService waterIntakeService;

    public DailySummaryController(MealLogService mealLogService, WaterIntakeService waterIntakeService) {
        this.mealLogService = mealLogService;
        this.waterIntakeService = waterIntakeService;
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyNutritionSummary> getDailySummary(
            @RequestParam Long userId,
            @RequestParam String date // yyyy-MM-dd format
    ) {
        LocalDate localDate = LocalDate.parse(date);

        DailyNutritionSummary summary = mealLogService.calculateDailySummary(userId, localDate);
        int totalWaterIntake = waterIntakeService.calculateDailyWaterIntake(userId, localDate);

        summary.setTotalWaterIntakeMl(totalWaterIntake);

        return ResponseEntity.ok(summary);
    }
}
