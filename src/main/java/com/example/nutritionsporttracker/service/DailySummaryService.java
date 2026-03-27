package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.model.DailySummary;
import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.model.WaterIntake;
import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.repository.DailySummaryRepository;
import com.example.nutritionsporttracker.repository.MealLogRepository;
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

    private final MealLogRepository mealLogRepository;
    private final WaterIntakeRepository waterIntakeRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final UserRepository userRepository;

    // ✅ Sprint 1: daily_summary tablosuna upsert için
    private final DailySummaryRepository dailySummaryRepository;

    public DailySummaryService(MealLogRepository mealLogRepository,
                               WaterIntakeRepository waterIntakeRepository,
                               WorkoutLogRepository workoutLogRepository,
                               UserRepository userRepository,
                               DailySummaryRepository dailySummaryRepository) {
        this.mealLogRepository = mealLogRepository;
        this.waterIntakeRepository = waterIntakeRepository;
        this.workoutLogRepository = workoutLogRepository;
        this.userRepository = userRepository;
        this.dailySummaryRepository = dailySummaryRepository;
    }

    // ============ EMAIL TABANLI API ============

    public Optional<DailySummaryDto> getTodaySummaryDto(String email) {
        return getDailySummaryDto(email, LocalDate.now());
    }

    public Optional<DailySummaryDto> getYesterdaySummaryDto(String email) {
        return getDailySummaryDto(email, LocalDate.now().minusDays(1));
    }

    public Optional<DailySummaryDto> getDailySummaryDto(String email, LocalDate date) {
        if (email == null || email.isBlank()) return Optional.empty();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();

        return getDailySummaryDto(userOpt.get().getId(), date);
    }

    // ============ CORE (ID TABANLI) ============

    public Optional<DailySummaryDto> getDailySummaryDto(Long userId, LocalDate date) {
        if (userId == null || date == null) return Optional.empty();

        // user var mı?
        if (userRepository.findById(userId).isEmpty()) return Optional.empty();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        // Öğün verileri
        List<MealLog> meals = mealLogRepository.findByUserIdAndCreatedAtBetween(userId, start, end);
        double totalCalories = meals.stream().mapToDouble(MealLog::getCalories).sum();
        double totalProtein  = meals.stream().mapToDouble(MealLog::getProtein).sum();
        double totalCarbs    = meals.stream().mapToDouble(MealLog::getCarbs).sum();
        double totalFat      = meals.stream().mapToDouble(MealLog::getFat).sum();

        // Su verileri
        List<WaterIntake> waterLogs = waterIntakeRepository.findByUserIdAndCreatedAtBetween(userId, start, end);
        double totalWater = waterLogs.stream().mapToDouble(WaterIntake::getAmountMl).sum();

        // Egzersiz verileri
        List<WorkoutLog> workouts = workoutLogRepository.findByUserIdAndCreatedAtBetween(userId, start, end);

        double totalWorkoutMinutes = workouts.stream()
                .mapToDouble(w -> w.getDurationMinutes() == null ? 0 : w.getDurationMinutes())
                .sum();

        double totalBurned = workouts.stream()
                .mapToDouble(w -> w.getCaloriesBurned() == null ? 0 : w.getCaloriesBurned())
                .sum();

        String exerciseSummary = workouts.isEmpty()
                ? "Egzersiz yapılmadı"
                : workouts.stream()
                .map(w -> w.getExerciseName() == null ? "Unknown" : w.getExerciseName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Egzersiz yapılmadı");

        DailySummaryDto dto = new DailySummaryDto();
        dto.setUserId(userId);
        dto.setDate(date);
        dto.setTotalCalories(totalCalories);
        dto.setProtein(totalProtein);
        dto.setCarbs(totalCarbs);
        dto.setFat(totalFat);
        dto.setTotalWater(totalWater);
        dto.setTotalWorkoutMinutes(totalWorkoutMinutes);
        dto.setTotalBurned(totalBurned);
        dto.setExerciseSummary(exerciseSummary);

        return Optional.of(dto);
    }

    // ============ SPRINT 1: DAILY SUMMARY ENTITY UPSERT ============

    /**
     * ✅ Sprint 1: Bu metod günlük verileri hesaplar ve daily_summary tablosuna kaydeder.
     * Aynı user + aynı date varsa update eder (upsert).
     */
    public Optional<DailySummary> createOrUpdateDailySummary(Long userId, LocalDate date) {
        if (userId == null || date == null) return Optional.empty();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return Optional.empty();

        Optional<DailySummaryDto> dtoOpt = getDailySummaryDto(userId, date);
        if (dtoOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();
        DailySummaryDto dto = dtoOpt.get();

        DailySummary summary = dailySummaryRepository
                .findByUser_IdAndDate(userId, date)
                .orElseGet(DailySummary::new);

        summary.setUser(user);
        summary.setDate(date);
        summary.setTotalCalories(dto.getTotalCalories());
        summary.setTotalBurned(dto.getTotalBurned());
        summary.setTotalWater(dto.getTotalWater());

        // aiReportText / aiGeneratedAt burada set etmiyoruz; DailyReportService set edecek.

        DailySummary saved = dailySummaryRepository.save(summary);
        return Optional.of(saved);
    }

    /**
     * ✅ Helper: DailySummary entity değiştiyse (AI text vs.) persist etmek için.
     */
    public DailySummary saveDailySummaryEntity(DailySummary summary) {
        return dailySummaryRepository.save(summary);
    }

    // Gün sonunda otomatik özet oluşturmak için (opsiyonel)
    public void generateDailySummary() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        userRepository.findAll()
                .forEach(u -> createOrUpdateDailySummary(u.getId(), yesterday));
    }
}