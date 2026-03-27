package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.service.DailyReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dev")
public class DevToolController {

    private final DailyReportService dailyReportService;

    public DevToolController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    // ✅ BUGÜN için tüm kullanıcılara rapor (GET)
    // GET /api/dev/test-daily-report
    @GetMapping("/test-daily-report")
    public String triggerDailyReport() {
        dailyReportService.generateAndSendReportsForAllUsers(LocalDate.now());
        return "Günlük rapor (bugün) oluşturuldu, mail gönderildi ve veritabanına kaydedildi.";
    }

    // ✅ DÜN için tüm kullanıcılara rapor (GET)
    // GET /api/dev/test-daily-report/yesterday
    @GetMapping("/test-daily-report/yesterday")
    public String triggerDailyReportYesterday() {
        dailyReportService.generateAndSendReportsForAllUsers(LocalDate.now().minusDays(1));
        return "Günlük rapor (dün) oluşturuldu, mail gönderildi ve veritabanına kaydedildi.";
    }

    // ✅ Belirli kullanıcı için BUGÜN raporu (GET)
    // GET /api/dev/test-daily-report/{userId}
    @GetMapping("/test-daily-report/{userId}")
    public String triggerDailyReportForUser(@PathVariable Long userId) {
        dailyReportService.generateAndSendReportForUser(userId, LocalDate.now());
        return "Kullanıcıya özel günlük rapor (bugün) oluşturuldu ve gönderildi.";
    }

    // ✅ Belirli kullanıcı için İSTENEN TARİH raporu (GET)
    // GET /api/dev/test-daily-report/{userId}/date?date=2026-02-25
    @GetMapping("/test-daily-report/{userId}/date")
    public String triggerDailyReportForUserWithDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        dailyReportService.generateAndSendReportForUser(userId, date);
        return "Kullanıcıya özel günlük rapor (" + date + ") oluşturuldu ve gönderildi.";
    }
}