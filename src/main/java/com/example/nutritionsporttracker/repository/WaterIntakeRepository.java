package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
    List<WaterIntake> findByUserId(Long userId);
}
