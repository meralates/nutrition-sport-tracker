/*
package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeeklyReportScheduler {

    private final UserRepository userRepository;
    private final ReportService reportService;

    public WeeklyReportScheduler(UserRepository userRepository, ReportService reportService) {
        this.userRepository = userRepository;
        this.reportService = reportService;
    }

    // ✅ Her Pazartesi 08:30 Europe/Istanbul
    @Scheduled(cron = "0 30 8 * * MON", zone = "Europe/Istanbul")
    public void runWeekly() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getEmail() == null || user.getEmail().isBlank()) continue;

            try {
                reportService.generateWeeklyReportByEmail(user.getEmail()); // ✅ email gönder
      f66666      } catch (Exception ignored) {
                // istersen log ekleriz
            }
        }
    }
}
*/