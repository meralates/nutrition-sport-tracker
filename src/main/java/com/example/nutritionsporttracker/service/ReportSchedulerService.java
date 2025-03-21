package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportSchedulerService {

    private final ReportService reportService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public ReportSchedulerService(ReportService reportService, EmailService emailService, UserRepository userRepository) {
        this.reportService = reportService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    // 📌 Günlük raporları her gece 23:59'da gönder
    @Scheduled(cron = "59 59 23 * * ?")
    public void scheduleDailyReport() {
        System.out.println("📢 Günlük rapor cron job çalışıyor...");

        List<User> users = userRepository.findAll();

        for (User user : users) {
            Reports report = reportService.generateWeeklyReport(user.getId());

            String emailBody = "Dear " + user.getFullName() + ",\n\n" +
                    "Here is your daily report:\n\n" + report.getReportText();

            emailService.sendEmail(user.getEmail(), "Your Daily Report", emailBody);
            System.out.println("📧 Günlük rapor gönderildi: " + user.getEmail());
        }
    }

    // 📌 Haftalık raporları her Pazar 23:00'da gönder
    @Scheduled(cron = "0 0 23 ? * SUN")
    public void scheduleWeeklyReport() {
        System.out.println("📢 Haftalık rapor cron job çalışıyor...");

        List<User> users = userRepository.findAll();

        for (User user : users) {
            Reports report = reportService.generateWeeklyReport(user.getId());

            String emailBody = "Dear " + user.getFullName() + ",\n\n" +
                    "Here is your weekly report:\n\n" + report.getReportText();

            emailService.sendEmail(user.getEmail(), "Your Weekly Report", emailBody);
            System.out.println("📧 Haftalık rapor gönderildi: " + user.getEmail());
        }
    }
}
