package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.Recommendations;
import com.example.nutritionsporttracker.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // Kullanıcı için öneri ekleme
    @PostMapping
    public ResponseEntity<Recommendations> createRecommendation(@RequestBody Recommendations recommendation) {
        return new ResponseEntity<>(recommendationService.addRecommendation(recommendation), HttpStatus.CREATED);
    }

    // Kullanıcı ID'sine göre tüm önerileri listeleme
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendations>> getRecommendationsByUserId(@PathVariable Long userId) {
        List<Recommendations> recommendations = recommendationService.getRecommendationsByUserId(userId);
        if (recommendations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(recommendations);
    }
}
