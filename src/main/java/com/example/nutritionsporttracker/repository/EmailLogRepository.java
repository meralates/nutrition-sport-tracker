package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}