package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.MealTimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserId(Long userId);
    List<MealLog> findByUserIdAndMealTime(Long userId, MealTimeType mealTime);
}
