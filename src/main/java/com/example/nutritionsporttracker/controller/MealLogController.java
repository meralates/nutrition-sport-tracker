package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.FoodSearchResponse;
import com.example.nutritionsporttracker.dto.MealLogRequest;
import com.example.nutritionsporttracker.dto.MealLogResponse;
import com.example.nutritionsporttracker.security.CustomUserDetails;
import com.example.nutritionsporttracker.service.MealLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meal")
public class MealLogController {

    private final MealLogService mealLogService;

    public MealLogController(MealLogService mealLogService) {
        this.mealLogService = mealLogService;
    }

    @GetMapping("/search-foods")
    public ResponseEntity<List<FoodSearchResponse>> searchFoods(
            @RequestParam String query
    ) {
        List<FoodSearchResponse> res = mealLogService.searchFoods(query);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/log")
    public ResponseEntity<MealLogResponse> addMeal(
            @AuthenticationPrincipal CustomUserDetails me,
            @Valid @RequestBody MealLogRequest request
    ) {
        MealLogResponse res = mealLogService.addMeal(request, me.getUsername());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/today")
    public ResponseEntity<List<MealLogResponse>> getToday(
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        List<MealLogResponse> res = mealLogService.getMealsToday(me.getUsername());
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<MealLogResponse>> getBetween(
            @AuthenticationPrincipal CustomUserDetails me,
            @RequestParam("dateFrom") LocalDate dateFrom,
            @RequestParam("dateTo") LocalDate dateTo
    ) {
        List<MealLogResponse> res = mealLogService.getMealsBetween(me.getUsername(), dateFrom, dateTo);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long mealId,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        mealLogService.deleteMeal(mealId, me.getUsername());
        return ResponseEntity.noContent().build();
    }
}