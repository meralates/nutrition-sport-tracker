package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
    @GetMapping("/daily")
    public ResponseEntity<String> getDailyRecommendation(@RequestParam String email) {
        String recommendation = recommendationService.generateDailyRecommendation(email);
        return ResponseEntity.ok(recommendation);
    }
}
