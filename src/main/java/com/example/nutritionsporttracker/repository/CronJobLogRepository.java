package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.CronJobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronJobLogRepository extends JpaRepository<CronJobLog, Long> {
}
