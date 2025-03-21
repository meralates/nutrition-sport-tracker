package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.MealTimeType;
import com.example.nutritionsporttracker.repository.MealLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MealLogService {

    private final MealLogRepository mealLogRepository;

    public MealLogService(MealLogRepository mealLogRepository) {
        this.mealLogRepository = mealLogRepository;
    }

    public MealLog addMealLog(MealLog mealLog) {
        return mealLogRepository.save(mealLog);
    }

    public List<MealLog> getMealLogsByUserId(Long userId) {
        return mealLogRepository.findByUserId(userId);
    }

    // Kullanıcının belirli bir öğün geçmişini çekme
    public List<MealLog> getMealLogsByUserIdAndMealTime(Long userId, MealTimeType mealTime) {
        return mealLogRepository.findByUserIdAndMealTime(userId, mealTime);
    }
}
