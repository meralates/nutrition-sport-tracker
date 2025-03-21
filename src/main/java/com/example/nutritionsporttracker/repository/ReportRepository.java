package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Reports, Long> {

    List<Reports> findByGeneratedAtAfter(LocalDateTime startDate);

    List<Reports> findByUserId(Long userId);
}
