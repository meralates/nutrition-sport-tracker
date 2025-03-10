package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.Reports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Reports, Long> {
    List<Reports> findByUserId(Long userId);
}
