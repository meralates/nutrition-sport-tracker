package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.FoodSearchResponse;
import com.example.nutritionsporttracker.dto.MealLogRequest;
import com.example.nutritionsporttracker.dto.MealLogResponse;
import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.MealLogRepository;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealLogService {

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final UsdaFoodService usdaFoodService;

    public MealLogService(MealLogRepository mealLogRepository,
                          UserRepository userRepository,
                          UsdaFoodService usdaFoodService) {
        this.mealLogRepository = mealLogRepository;
        this.userRepository = userRepository;
        this.usdaFoodService = usdaFoodService;
    }

    public List<FoodSearchResponse> searchFoods(String query) {
        List<JsonNode> foods = usdaFoodService.searchFoods(query);

        return foods.stream()
                .limit(5)
                .map(food -> new FoodSearchResponse(
                        food.path("fdcId").asText(""),
                        food.path("description").asText("Unknown Food"),
                        usdaFoodService.extractNutrient(food, "Energy", "Energy (Atwater General)"),
                        usdaFoodService.extractNutrient(food, "Protein"),
                        usdaFoodService.extractNutrient(food, "Carbohydrate, by difference"),
                        usdaFoodService.extractNutrient(food, "Total lipid (fat)")
                ))
                .collect(Collectors.toList());
    }

    public MealLogResponse addMeal(MealLogRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MealLog log = new MealLog();
        log.setUser(user);
        log.setFoodName(request.getFoodName());
        log.setMealTime(request.getMealTime());
        log.setGrams(request.getGrams());
        log.setCalories(round1(request.getCalories()));
        log.setProtein(round1(request.getProtein()));
        log.setCarbs(round1(request.getCarbs()));
        log.setFat(round1(request.getFat()));
        log.setSourceFoodId(request.getSourceFoodId());

        MealLog saved = mealLogRepository.save(log);
        return toResponse(saved);
    }

    public List<MealLogResponse> getMealsToday(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        List<MealLog> logs = mealLogRepository.findByUserIdAndCreatedAtBetween(user.getId(), start, end);

        return logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MealLogResponse> getMealsBetween(String email, LocalDate dateFrom, LocalDate dateTo) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime start = dateFrom.atStartOfDay();
        LocalDateTime end = dateTo.atTime(LocalTime.MAX);

        List<MealLog> logs = mealLogRepository.findByUserIdAndCreatedAtBetween(user.getId(), start, end);

        return logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteMeal(Long mealId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MealLog mealLog = mealLogRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal record not found"));

        if (mealLog.getUser() == null || !mealLog.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this meal record");
        }

        mealLogRepository.delete(mealLog);
    }

    private MealLogResponse toResponse(MealLog saved) {
        return new MealLogResponse(
                saved.getId(),
                saved.getFoodName(),
                saved.getCalories(),
                saved.getProtein(),
                saved.getCarbs(),
                saved.getFat(),
                saved.getGrams(),
                saved.getMealTime(),
                saved.getCreatedAt() != null ? saved.getCreatedAt().toString() : null
        );
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}