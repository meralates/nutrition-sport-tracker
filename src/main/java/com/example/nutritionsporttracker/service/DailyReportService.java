package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.DailySummary;
import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.DailySummaryRepository;
import com.example.nutritionsporttracker.repository.ReportRepository;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.example.nutritionsporttracker.util.DailyPromptBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyReportService {

    private final UserRepository userRepository;
    private final DailySummaryRepository dailySummaryRepository;
    private final ReportRepository reportRepository;
    private final OpenRouterService openRouterService;
    private final EmailService emailService;

    public DailyReportService(UserRepository userRepository,
                              DailySummaryRepository dailySummaryRepository,
                              ReportRepository reportRepository,
                              OpenRouterService openRouterService,
                              EmailService emailService) {
        this.userRepository = userRepository;
        this.dailySummaryRepository = dailySummaryRepository;
        this.reportRepository = reportRepository;
        this.openRouterService = openRouterService;
        this.emailService = emailService;
    }

    public void generateAndSendReports() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            dailySummaryRepository.findByUser_IdAndDate(user.getId(), LocalDate.now()).ifPresent(summary -> {
                String prompt = DailyPromptBuilder.build(user, summary);
                String reportText = null;
                try {
                    reportText = openRouterService.sendOpenRouterRequest(prompt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Mail gönder
                emailService.sendEmail(user.getEmail(), "Günlük Sağlık Raporun", reportText);

                // Veritabanına kaydet
                Reports report = new Reports();
                report.setUser(user);
                report.setReportText(reportText);
                report.setCaloriesIn(summary.getTotalCalories());
                report.setCaloriesOut(summary.getTotalBurned());
                report.setAvgWater(summary.getTotalWater());
                report.setGeneratedAt(LocalDateTime.now());

                reportRepository.save(report);
            });
        }
    }
    public void generateAndSendReportForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Kullanıcı bulunamadı"));

        dailySummaryRepository.findByUser_IdAndDate(userId, LocalDate.now()).ifPresent(summary -> {
            String prompt = DailyPromptBuilder.build(user, summary);
            String reportText = null;
            try {
                reportText = openRouterService.sendOpenRouterRequest(prompt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            emailService.sendEmail(user.getEmail(), "Günlük Sağlık Raporun", reportText);

            Reports report = new Reports();
            report.setUser(user);
            report.setReportText(reportText);
            report.setCaloriesIn(summary.getTotalCalories());
            report.setCaloriesOut(summary.getTotalBurned());
            report.setAvgWater(summary.getTotalWater());
            report.setGeneratedAt(LocalDateTime.now());

            reportRepository.save(report);
        });
    }

}
