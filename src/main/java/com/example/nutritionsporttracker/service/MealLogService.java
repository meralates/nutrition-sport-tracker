package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.repository.MealLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealLogService {

    @Autowired
    private MealLogRepository mealLogRepository;

    public MealLog addMealLog(MealLog mealLog) {
        return mealLogRepository.save(mealLog);
    }

    public List<MealLog> getMealLogsByUserId(Long userId) {
        return mealLogRepository.findByUserId(userId);
    }
}
