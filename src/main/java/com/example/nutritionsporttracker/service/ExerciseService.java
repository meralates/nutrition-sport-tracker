package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.model.WorkoutLog;
import com.example.nutritionsporttracker.model.ExerciseType;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.example.nutritionsporttracker.repository.WorkoutLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExerciseService {

    @Value("${nutritionix.app.id}")
    private String appId;

    @Value("${nutritionix.api.key}") // <== Burada değiştirildi
    private String appKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final WorkoutLogRepository workoutLogRepository;

    public ExerciseService(UserRepository userRepository,
                           WorkoutLogRepository workoutLogRepository) {
        this.userRepository = userRepository;
        this.workoutLogRepository = workoutLogRepository;
    }

    public List<WorkoutLog> processExercise(Long userId, String query) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-app-id", appId);
        headers.set("x-app-key", appKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("gender", user.getGender());
        body.put("weight_kg", user.getWeight());
        body.put("height_cm", user.getHeight());
        body.put("age", user.getAge());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        String url = "https://trackapi.nutritionix.com/v2/natural/exercise";

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                JsonNode.class
        );

        JsonNode exercises = response.getBody().get("exercises");
        List<WorkoutLog> savedLogs = new ArrayList<>();

        for (JsonNode ex : exercises) {
            WorkoutLog log = new WorkoutLog();
            log.setUser(user);
            log.setExerciseName(ex.get("name").asText());
            log.setDurationMinutes(ex.get("duration_min").asInt());
            log.setCaloriesBurned(ex.get("nf_calories").asDouble());
            log.setExerciseType(ExerciseType.OTHER);
            log.setCreatedAt(LocalDateTime.now());

            savedLogs.add(workoutLogRepository.save(log));
        }

        return savedLogs;
    }
}
