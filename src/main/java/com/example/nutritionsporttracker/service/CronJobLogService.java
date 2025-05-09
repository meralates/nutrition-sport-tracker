package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.CronJobLog;
import com.example.nutritionsporttracker.repository.CronJobLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CronJobLogService {

    private final CronJobLogRepository repository;

    public CronJobLogService(CronJobLogRepository repository) {
        this.repository = repository;
    }

    public void log(String jobName, String status) {
        CronJobLog log = new CronJobLog();
        log.setJobName(jobName);
        log.setStatus(status);
        log.setExecutedAt(LocalDateTime.now());
        repository.save(log);
    }
}
