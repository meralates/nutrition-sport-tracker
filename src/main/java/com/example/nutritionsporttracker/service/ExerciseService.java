package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.ExerciseType;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.example.nutritionsporttracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final UserRepository userRepository;
    private final WorkoutLogRepository workoutLogRepository;

    public ExerciseService(UserRepository userRepository,
                           WorkoutLogRepository workoutLogRepository) {
        this.userRepository = userRepository;
        this.workoutLogRepository = workoutLogRepository;
    }

    public WorkoutLog logExercise(Long userId,
                                  ExerciseType type,
                                  String exerciseName,
                                  Integer durationMinutes) {

        if (durationMinutes == null || durationMinutes < 1) {
            throw new IllegalArgumentException("durationMinutes must be >= 1");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        double weightKg = (user.getWeight() != null && user.getWeight() > 0)
                ? user.getWeight()
                : 70.0;

        double met = metFor(type);
        double calories = calcCalories(met, weightKg, durationMinutes);

        WorkoutLog log = new WorkoutLog();
        log.setUser(user);
        log.setExerciseType(type);
        log.setDurationMinutes(durationMinutes);
        log.setCaloriesBurned(round1(calories));

        String name = (exerciseName != null && !exerciseName.isBlank())
                ? exerciseName.trim()
                : defaultNameFor(type);

        log.setExerciseName(name);

        return workoutLogRepository.save(log);
    }

    public List<WorkoutLog> getLogsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Kullanıcı bulunamadı");
        }
        return workoutLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void deleteExercise(Long logId, Long userId) {
        WorkoutLog workoutLog = workoutLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Egzersiz kaydı bulunamadı"));

        if (workoutLog.getUser() == null || !workoutLog.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu egzersiz kaydını silme yetkiniz yok");
        }

        workoutLogRepository.delete(workoutLog);
    }

    private double metFor(ExerciseType type) {
        return switch (type) {
            case CARDIO -> 8.0;
            case STRENGTH -> 6.0;
            case FLEXIBILITY -> 2.5;
            case BALANCE -> 3.0;
            case OTHER -> 5.0;
        };
    }

    private String defaultNameFor(ExerciseType type) {
        return switch (type) {
            case CARDIO -> "Cardio";
            case STRENGTH -> "Strength";
            case FLEXIBILITY -> "Flexibility";
            case BALANCE -> "Balance";
            case OTHER -> "Workout";
        };
    }

    private double calcCalories(double met, double weightKg, int minutes) {
        return met * 3.5 * weightKg / 200.0 * minutes;
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}