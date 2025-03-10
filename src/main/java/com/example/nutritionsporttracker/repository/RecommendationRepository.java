package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendations, Long> {
    List<Recommendations> findByUserId(Long userId);
}
