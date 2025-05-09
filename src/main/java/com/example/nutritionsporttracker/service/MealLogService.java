package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.DailyNutritionSummary;
import com.example.nutritionsporttracker.dto.FoodItem;
import com.example.nutritionsporttracker.dto.MealLogRequest;
import com.example.nutritionsporttracker.dto.MealLogResponse;
import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.MealTimeType;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.MealLogRepository;
import com.example.nutritionsporttracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealLogService {

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final NutritionixService nutritionixService;

    public MealLogService(MealLogRepository mealLogRepository,
                          UserRepository userRepository,
                          NutritionixService nutritionixService) {
        this.mealLogRepository = mealLogRepository;
        this.userRepository = userRepository;
        this.nutritionixService = nutritionixService;
    }

    public MealLogResponse addMealLog(MealLogRequest request) {
        // 1. User'ı getir
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 2. Nutritionix API'den besin bilgisi al
        FoodItem food = nutritionixService.searchProductByName(request.getQuery())
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Besin bulunamadı"))
                .getFoods()
                .get(0); // İlk sonucu al

        // 3. MealLog nesnesi oluştur
        MealLog mealLog = new MealLog();
        mealLog.setUser(user);
        mealLog.setFoodName(food.getFoodName());
        mealLog.setCalories(food.getCalories());
        mealLog.setProtein(food.getProtein());
        mealLog.setCarbs(food.getCarbs());
        mealLog.setFat(food.getFat());
        mealLog.setMealTime(MealTimeType.valueOf(request.getMealTime()));

        // 4. Kaydet ve güncelle
        MealLog savedMealLog = mealLogRepository.save(mealLog);
        updateUserWithDailyTotals(user.getId(), savedMealLog.getCreatedAt().toLocalDate());

        // 5. Yanıt DTO'su dön
        MealLogResponse response = new MealLogResponse();
        response.setFoodName(savedMealLog.getFoodName());
        response.setCalories(savedMealLog.getCalories());
        response.setProtein(savedMealLog.getProtein());
        response.setCarbs(savedMealLog.getCarbs());
        response.setFat(savedMealLog.getFat());
        response.setMealTime(savedMealLog.getMealTime().name());
        response.setCreatedAt(savedMealLog.getCreatedAt().toString());

        return response;
    }


    public List<MealLog> getMealLogsByUserId(Long userId) {
        return mealLogRepository.findByUserId(userId);
    }

    public List<MealLog> getMealLogsByUserIdAndMealTime(Long userId, MealTimeType mealTime) {
        return mealLogRepository.findByUserIdAndMealTime(userId, mealTime);
    }

    public List<MealLog> getMealLogsByUserIdAndDate(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return mealLogRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);
    }

    public DailyNutritionSummary calculateDailySummary(Long userId, LocalDate date) {
        List<MealLog> mealLogs = getMealLogsByUserIdAndDate(userId, date);

        DailyNutritionSummary summary = new DailyNutritionSummary();

        if (mealLogs == null || mealLogs.isEmpty()) {
            summary.setTotalCalories(0);
            summary.setTotalProtein(0.0);
            summary.setTotalCarbs(0.0);
            summary.setTotalFat(0.0);
            return summary;
        }

        int totalCalories = mealLogs.stream()
                .mapToInt(m -> (int) m.getCalories())
                .sum();

        double totalProtein = mealLogs.stream()
                .mapToDouble(MealLog::getProtein)
                .sum();

        double totalCarbs = mealLogs.stream()
                .mapToDouble(MealLog::getCarbs)
                .sum();

        double totalFat = mealLogs.stream()
                .mapToDouble(MealLog::getFat)
                .sum();

        summary.setTotalCalories(totalCalories);
        summary.setTotalProtein(totalProtein);
        summary.setTotalCarbs(totalCarbs);
        summary.setTotalFat(totalFat);

        return summary;
    }

    private void updateUserWithDailyTotals(Long userId, LocalDate date) {
        List<MealLog> todaysLogs = getMealLogsByUserIdAndDate(userId, date);

        int totalCalories = todaysLogs.stream()
                .mapToInt(m -> (int) m.getCalories())
                .sum();

        double totalProtein = todaysLogs.stream()
                .mapToDouble(MealLog::getProtein)
                .sum();

        double totalCarbs = todaysLogs.stream()
                .mapToDouble(MealLog::getCarbs)
                .sum();

        double totalFat = todaysLogs.stream()
                .mapToDouble(MealLog::getFat)
                .sum();

        userRepository.findById(userId).ifPresent(user -> {
            user.setDailyCalories((double) totalCalories);
            user.setProtein(totalProtein);
            user.setCarbs(totalCarbs);
            user.setFat(totalFat);
            userRepository.save(user);
        });
    }
}
