package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.model.*;
import com.example.nutritionsporttracker.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DailySummaryService {

    private static final Logger logger = LoggerFactory.getLogger(DailySummaryService.class);

    private final UserRepository userRepository;
    private final MealLogRepository mealLogRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final WaterIntakeRepository waterIntakeRepository;
    private final DailySummaryRepository dailySummaryRepository;

    public DailySummaryService(UserRepository userRepository,
                               MealLogRepository mealLogRepository,
                               WorkoutLogRepository workoutLogRepository,
                               WaterIntakeRepository waterIntakeRepository,
                               DailySummaryRepository dailySummaryRepository) {
        this.userRepository = userRepository;
        this.mealLogRepository = mealLogRepository;
        this.workoutLogRepository = workoutLogRepository;
        this.waterIntakeRepository = waterIntakeRepository;
        this.dailySummaryRepository = dailySummaryRepository;
    }

    public void generateDailySummary() {
        logger.info("Günlük özet oluşturma işlemi başladı.");

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<User> users = userRepository.findAll();

        for (User user : users) {
            Long userId = user.getId();

            List<MealLog> todaysMeals = mealLogRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);
            List<WorkoutLog> todaysWorkouts = workoutLogRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);
            List<WaterIntake> todaysWater = waterIntakeRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);

            double totalCalories = todaysMeals.stream().mapToDouble(MealLog::getCalories).sum();
            double totalBurned = todaysWorkouts.stream().mapToDouble(WorkoutLog::getCaloriesBurned).sum();
            double totalWater = todaysWater.stream().mapToDouble(WaterIntake::getAmountMl).sum();

            DailySummary summary = new DailySummary();
            summary.setUser(user);
            summary.setDate(today);
            summary.setTotalCalories(totalCalories);
            summary.setTotalBurned(totalBurned);
            summary.setTotalWater(totalWater);

            dailySummaryRepository.save(summary);

            logger.info("Kullanıcı: {} - Kalori: {}, Yakılan: {}, Su: {} ml",
                    user.getFullName(), totalCalories, totalBurned, totalWater);
        }

        logger.info("Günlük özet oluşturma tamamlandı.");
    }

    public Optional<DailySummaryDto> getDailySummaryDto(Long userId, LocalDate date) {
        return dailySummaryRepository.findByUser_IdAndDate(userId, date)
                .map(summary -> {
                    DailySummaryDto dto = new DailySummaryDto();
                    dto.setUserId(summary.getUser().getId());
                    dto.setUserName(summary.getUser().getFullName());
                    dto.setDate(summary.getDate());
                    dto.setTotalCalories(summary.getTotalCalories());
                    dto.setTotalBurned(summary.getTotalBurned());
                    dto.setTotalWater(summary.getTotalWater());
                    return dto;
                });
    }
}
