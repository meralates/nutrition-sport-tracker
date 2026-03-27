package com.example.nutritionsporttracker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DailyReportScheduler {

    private final DailyReportService dailyReportService;

    public DailyReportScheduler(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    // ✅ Her gün 23:00 Europe/Istanbul (Sprint 1)
    @Scheduled(cron = "0 0 23 * * *", zone = "Europe/Istanbul")
    public void run() {
        dailyReportService.generateAndSendReportsForAllUsers(LocalDate.now());
    }
}