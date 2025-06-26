package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.CronJobLog;
import com.example.nutritionsporttracker.repository.CronJobLogRepository;
import com.example.nutritionsporttracker.service.DailyReportService;
import com.example.nutritionsporttracker.service.DailySummaryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CronJobService {

    private final DailySummaryService dailySummaryService;
    private final DailyReportService dailyReportService;
    private final CronJobLogRepository cronJobLogRepository;

    public CronJobService(DailySummaryService dailySummaryService,
                          DailyReportService dailyReportService,
                          CronJobLogRepository cronJobLogRepository) {
        this.dailySummaryService = dailySummaryService;
        this.dailyReportService = dailyReportService;
        this.cronJobLogRepository = cronJobLogRepository;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void runDailySummaryAndReportJob() {
        CronJobLog log = new CronJobLog();
        log.setJobName("DailySummaryAndReportJob");
        log.setExecutedAt(LocalDateTime.now());

        try {
            System.out.println("📣 Cron job başlatıldı: Günlük özetler ve raporlar oluşturuluyor...");
            dailySummaryService.generateDailySummary();
            dailyReportService.generateAndSendReports();
            log.setStatus("SUCCESS");
        } catch (Exception e) {
            log.setStatus("FAILURE: " + e.getMessage());
            e.printStackTrace();
        }

        cronJobLogRepository.save(log);
    }
}
