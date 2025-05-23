package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.model.DailySummary;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.DailySummaryRepository;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.example.nutritionsporttracker.repository.WaterIntakeRepository;
import com.example.nutritionsporttracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DailySummaryService {

    private final UserRepository userRepository;
    private final MealLogService mealLogService;
    private final WorkoutLogRepository workoutLogRepository;
    private final WaterIntakeRepository waterIntakeRepository;
    private final DailySummaryRepository dailySummaryRepository;

    public DailySummaryService(UserRepository userRepository,
                               MealLogService mealLogService,
                               WorkoutLogRepository workoutLogRepository,
                               WaterIntakeRepository waterIntakeRepository,
                               DailySummaryRepository dailySummaryRepository) {
        this.userRepository = userRepository;
        this.mealLogService = mealLogService;
        this.workoutLogRepository = workoutLogRepository;
        this.waterIntakeRepository = waterIntakeRepository;
        this.dailySummaryRepository = dailySummaryRepository;
    }

    public void generateDailySummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        List<User> users = userRepository.findAll();

        for (User user : users) {
            Long userId = user.getId();

            // 1. Kalori alımı
            int totalCalories = mealLogService.getMealLogsByUserIdAndDate(userId, today)
                    .stream().mapToInt(m -> (int) m.getCalories()).sum();

            // 2. Yakılan kalori
            double totalBurned = workoutLogRepository
                    .findByUserIdAndCreatedAtBetween(userId, start, end)
                    .stream().mapToDouble(w -> w.getCaloriesBurned()).sum();

            // 3. Su miktarı
            double totalWater = waterIntakeRepository
                    .findByUserIdAndCreatedAtBetween(userId, start, end)
                    .stream().mapToInt(w -> w.getAmountMl()).sum();

            // 4. Summary kaydı
            DailySummary summary = new DailySummary();
            summary.setUser(user);
            summary.setDate(today);
            summary.setTotalCalories(totalCalories);
            summary.setTotalBurned(totalBurned);
            summary.setTotalWater(totalWater);

            dailySummaryRepository.save(summary);
        }
    }
    public Optional<DailySummaryDto> getDailySummaryDto(Long userId, LocalDate date) {
        return dailySummaryRepository.findByUser_IdAndDate(userId, date)
                .map(summary -> new DailySummaryDto(
                        summary.getUser().getId(),
                        summary.getUser().getFullName(),
                        summary.getDate(),
                        summary.getTotalCalories(),
                        summary.getTotalBurned(),
                        summary.getTotalWater()
                ));
    }

}
