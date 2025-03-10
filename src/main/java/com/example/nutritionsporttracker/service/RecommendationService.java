package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.Recommendations;
import com.example.nutritionsporttracker.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    public Recommendations addRecommendation(Recommendations recommendation) {
        return recommendationRepository.save(recommendation);
    }

    public List<Recommendations> getRecommendationsByUserId(Long userId) {
        return recommendationRepository.findByUserId(userId);
    }
}
