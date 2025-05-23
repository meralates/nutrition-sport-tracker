package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.service.DailyReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devtools")
public class DevToolController {

    private final DailyReportService dailyReportService;

    public DevToolController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @GetMapping("/test-daily-report")
    public String triggerDailyReport() {
        dailyReportService.generateAndSendReports();
        return "LLM raporu oluşturuldu, mail gönderildi ve veritabanına kaydedildi.";
    }
    @GetMapping("/test-daily-report/{userId}")
    public String triggerDailyReportForUser(@PathVariable Long userId) {
        dailyReportService.generateAndSendReportForUser(userId);
        return "Kullanıcıya özel LLM raporu oluşturuldu ve gönderildi.";
    }

}
