package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
    Optional<DailySummary> findByUser_IdAndDate(Long userId, LocalDate date);
}
