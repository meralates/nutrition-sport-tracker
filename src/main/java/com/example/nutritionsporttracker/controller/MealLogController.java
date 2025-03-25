package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.FoodItem;
import com.example.nutritionsporttracker.dto.ProductSearchResponse;
import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.MealTimeType;
import com.example.nutritionsporttracker.service.MealLogService;
import com.example.nutritionsporttracker.service.NutritionixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
public class MealLogController {

    private final MealLogService mealLogService;
    private final NutritionixService nutritionixService;

    public MealLogController(MealLogService mealLogService, NutritionixService nutritionixService) {
        this.mealLogService = mealLogService;
        this.nutritionixService = nutritionixService;
    }

    @GetMapping("/history")//gecmişi
    public ResponseEntity<List<MealLog>> getMealHistory(@RequestParam Long userId) {
        List<MealLog> mealLogs = mealLogService.getMealLogsByUserId(userId);
        return mealLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(mealLogs);
    }

    @PostMapping("/meal")
    public ResponseEntity<Mono<MealLog>> addMealLog(@RequestParam Long userId, @RequestParam String foodName) {
        Mono<ProductSearchResponse> foodData = nutritionixService.searchProductByName(foodName);

        Mono<MealLog> mealLogMono = foodData.map(response -> {
            List<FoodItem> items = response.getCommon();
            if (items.isEmpty()) {
                throw new RuntimeException("Food item not found: " + foodName);
            }

            FoodItem foodItem = items.get(0);

            MealLog mealLog = new MealLog();
            mealLog.setUserId(userId);
            mealLog.setFoodName(foodItem.getFoodName());
            mealLog.setCalories(foodItem.getCalories());
            mealLog.setProtein(foodItem.getProtein());
            mealLog.setCarbs(foodItem.getCarbs());
            mealLog.setFat(foodItem.getFat());

            return mealLogService.addMealLog(mealLog);
        });

        return ResponseEntity.ok(mealLogMono);
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

}
