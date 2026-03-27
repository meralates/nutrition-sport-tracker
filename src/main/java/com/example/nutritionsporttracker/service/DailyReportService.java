package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.model.CronJobLog;
import com.example.nutritionsporttracker.model.DailySummary;
import com.example.nutritionsporttracker.model.EmailLog;
import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.CronJobLogRepository;
import com.example.nutritionsporttracker.repository.DailySummaryRepository;
import com.example.nutritionsporttracker.repository.EmailLogRepository;
import com.example.nutritionsporttracker.repository.ReportRepository;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.example.nutritionsporttracker.util.DailyEmailTemplateBuilder;
import com.example.nutritionsporttracker.util.DailyPromptBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DailyReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final DailySummaryService dailySummaryService;
    private final DailySummaryRepository dailySummaryRepository;
    private final OpenRouterClient openRouterClient;
    private final EmailService emailService;
    private final EmailLogRepository emailLogRepository;
    private final CronJobLogRepository cronJobLogRepository;

    public DailyReportService(UserRepository userRepository,
                              ReportRepository reportRepository,
                              DailySummaryService dailySummaryService,
                              DailySummaryRepository dailySummaryRepository,
                              OpenRouterClient openRouterClient,
                              EmailService emailService,
                              EmailLogRepository emailLogRepository,
                              CronJobLogRepository cronJobLogRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.dailySummaryService = dailySummaryService;
        this.dailySummaryRepository = dailySummaryRepository;
        this.openRouterClient = openRouterClient;
        this.emailService = emailService;
        this.emailLogRepository = emailLogRepository;
        this.cronJobLogRepository = cronJobLogRepository;
    }

    public void generateAndSendReportsForAllUsers(LocalDate targetDate) {
        String jobName = "DAILY_AI_EMAIL_REPORT";
        CronJobLog jobLog = new CronJobLog();
        jobLog.setJobName(jobName);
        jobLog.setExecutedAt(LocalDateTime.now());

        int sent = 0;
        int failed = 0;

        try {
            List<User> users = userRepository.findAll();

            for (User user : users) {
                String email = user.getEmail();
                if (email == null || email.isBlank()) continue;

                try {
                    processOneUser(user, email, targetDate);
                    sent++;
                } catch (Exception ex) {
                    failed++;

                    EmailLog elog = new EmailLog();
                    elog.setUserId(user.getId());
                    elog.setToEmail(email);
                    elog.setSubject("Günlük Sağlık Raporun — " + targetDate);
                    elog.setReportDate(targetDate);
                    elog.setStatus(EmailLog.Status.FAILED);
                    elog.setErrorMessage(ex.getMessage());
                    elog.setSentAt(LocalDateTime.now());
                    emailLogRepository.save(elog);
                }
            }

            jobLog.setStatus("SUCCESS (sent=" + sent + ", failed=" + failed + ")");
            cronJobLogRepository.save(jobLog);

        } catch (Exception fatal) {
            jobLog.setStatus("FATAL_ERROR: " + fatal.getMessage());
            cronJobLogRepository.save(jobLog);
        }
    }

    @Transactional
    protected void processOneUser(User user, String email, LocalDate targetDate) {

        Optional<DailySummaryDto> dtoOpt = dailySummaryService.getDailySummaryDto(user.getId(), targetDate);
        if (dtoOpt.isEmpty()) return;
        DailySummaryDto dto = dtoOpt.get();

        DailySummary summary = dailySummaryService.createOrUpdateDailySummary(user.getId(), targetDate)
                .orElseThrow(() -> new RuntimeException("DailySummary oluşturulamadı"));

        // Prompt + AI
        String prompt = DailyPromptBuilder.build(user, summary);
        String aiText = openRouterClient.complete(prompt);

        // ✅ 1) AI’ı DB’ye garanti yaz
        summary.setAiReportText(aiText);
        summary.setAiGeneratedAt(LocalDateTime.now());
        dailySummaryRepository.save(summary);

        // ✅ 2) Mail (patlarsa bile DB’de kalır)
        String subject = "Günlük Sağlık Raporun — " + targetDate;
        String html = DailyEmailTemplateBuilder.build(user, dto, aiText, targetDate);
        emailService.sendHtmlEmail(email, subject, html);

        // EmailLog (SENT)
        EmailLog elog = new EmailLog();
        elog.setUserId(user.getId());
        elog.setToEmail(email);
        elog.setSubject(subject);
        elog.setReportDate(targetDate);
        elog.setStatus(EmailLog.Status.SENT);
        elog.setSentAt(LocalDateTime.now());
        emailLogRepository.save(elog);

        // Reports opsiyonel
        Reports r = new Reports();
        r.setUser(user);
        r.setReportText(aiText);
        r.setCaloriesIn(dto.getTotalCalories());
        r.setCaloriesOut(dto.getTotalBurned());
        r.setAvgProtein(dto.getProtein());
        r.setAvgCarbs(dto.getCarbs());
        r.setAvgFat(dto.getFat());
        r.setAvgWater(dto.getTotalWater());
        r.setGeneratedAt(LocalDateTime.now());
        reportRepository.save(r);
    }

    public void generateAndSendReportForUser(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        String email = user.getEmail();
        if (email == null || email.isBlank()) throw new RuntimeException("Kullanıcının email'i yok");

        // aynı pipeline
        generateAndSendReportsForAllUsers(date); // istersen özel method yazabiliriz
    }
}