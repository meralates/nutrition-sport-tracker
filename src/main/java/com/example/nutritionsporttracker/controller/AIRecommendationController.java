package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.AiRequestData;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.service.AIRecommendationService;
import com.example.nutritionsporttracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIRecommendationController {

    private final AIRecommendationService aiRecommendationService;
    private final UserService userService;

    @Autowired
    public AIRecommendationController(AIRecommendationService aiRecommendationService, UserService userService) {
        this.aiRecommendationService = aiRecommendationService;
        this.userService = userService;
    }

    @GetMapping("/recommendation")
    public ResponseEntity<String> getRecommendation(@RequestParam String email) {
        Optional<User> userOpt = userService.findByEmail(email); //servis

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Kullanıcı bulunamadı.");
        }

        User user = userOpt.get();

        AiRequestData requestData = new AiRequestData(
                user.getAge(),
                user.getGender(),
                user.getHeight() != null ? user.getHeight().intValue() : 0,
                user.getWeight() != null ? user.getWeight().intValue() : 0,
                user.getActivityLevel() != null ? user.getActivityLevel().name() : "UNKNOWN",
                user.getGoal() != null ? user.getGoal().name() : "UNKNOWN",
                user.getDailyCalories() != null ? user.getDailyCalories().intValue() : 0,
                user.getCaloriesBurned() != null ? user.getCaloriesBurned().intValue() : 0,
                user.getProtein() != null ? user.getProtein().intValue() : 0,
                user.getCarbs() != null ? user.getCarbs().intValue() : 0,
                user.getFat() != null ? user.getFat().intValue() : 0,
                user.getWaterIntake() != null ? user.getWaterIntake() : 0.0,
                user.getExerciseDone() != null ? user.getExerciseDone().toString() : ""
        );

        String response = aiRecommendationService.getDailyRecommendation(requestData);

        return ResponseEntity.ok(response);
    }
}
