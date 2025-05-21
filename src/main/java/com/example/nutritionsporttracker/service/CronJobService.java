package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.CronJobLog;
import com.example.nutritionsporttracker.repository.CronJobLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CronJobService {

    private final DailySummaryService dailySummaryService;
    private final CronJobLogRepository cronJobLogRepository;

    public CronJobService(DailySummaryService dailySummaryService,
                          CronJobLogRepository cronJobLogRepository) {
        this.dailySummaryService = dailySummaryService;
        this.cronJobLogRepository = cronJobLogRepository;
    }

    // Her gün saat 08:00'de çalışacak
    @Scheduled(cron = "0 0 8 * * ?")
    public void runDailySummaryJob() {
        CronJobLog log = new CronJobLog();
        log.setJobName("DailySummaryJob");
        log.setExecutedAt(LocalDateTime.now());

        try {
            System.out.println("Cron job: Günlük özet başlatılıyor...");
            dailySummaryService.generateDailySummary();
            log.setStatus("SUCCESS");
        } catch (Exception e) {
            log.setStatus("FAILURE: " + e.getMessage());
            e.printStackTrace();
        }

        cronJobLogRepository.save(log);
    }
}
