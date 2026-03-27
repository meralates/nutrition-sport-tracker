package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.ReportRepository;
import com.example.nutritionsporttracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.ToDoubleFunction;

@Service
public class ReportService {

    private static final int WEEK_DURATION_DAYS = 7;

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // boşsa app patlamasın ama rapor üretirken hata verelim
    @Value("${openrouter.api.key:}")
    private String apiKey;

    @Value("${openrouter.model:mistralai/mistral-7b-instruct}")
    private String model;

    public ReportService(ReportRepository reportRepository,
                         UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    // ✅ JWT principal'dan gelen email ile haftalık rapor
    public Reports generateWeeklyReport(String email) {
        ensureOpenRouterEnabled();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));

        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(WEEK_DURATION_DAYS, ChronoUnit.DAYS);

        // ✅ Sadece bu user'ın son 7 günlük report'ları
        // Repo tarafına aşağıdaki methodu eklemen gerekiyor:
        // List<Reports> findByUserIdAndGeneratedAtAfter(Long userId, LocalDateTime startDate);
        List<Reports> weeklyReports = reportRepository.findByUserIdAndGeneratedAtAfter(user.getId(), oneWeekAgo);

        if (weeklyReports.isEmpty()) {
            throw new RuntimeException("Son 7 günde bu kullanıcı için rapor yok.");
        }

        ReportAverages averages = calculateAverages(weeklyReports);
        String aiText = getWeeklyAnalysisFromAI(user, averages);

        return saveReport(user, averages, aiText);
    }

    private void ensureOpenRouterEnabled() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "OpenRouter API key missing. application.properties içine openrouter.api.key eklemelisin."
            );
        }
    }

    private ReportAverages calculateAverages(List<Reports> reports) {
        return new ReportAverages(
                avg(reports, Reports::getCaloriesIn),
                avg(reports, Reports::getCaloriesOut),
                avg(reports, Reports::getAvgProtein),
                avg(reports, Reports::getAvgCarbs),
                avg(reports, Reports::getAvgFat),
                avg(reports, Reports::getAvgWater)
        );
    }

    private double avg(List<Reports> list, ToDoubleFunction<Reports> mapper) {
        return list.stream().mapToDouble(mapper).average().orElse(0.0);
    }

    private Reports saveReport(User user, ReportAverages avg, String aiText) {
        Reports report = new Reports();
        report.setUser(user);
        report.setGeneratedAt(LocalDateTime.now());
        report.setReportText(aiText);

        report.setCaloriesIn(avg.caloriesIn());
        report.setCaloriesOut(avg.caloriesOut());
        report.setAvgProtein(avg.protein());
        report.setAvgCarbs(avg.carbs());
        report.setAvgFat(avg.fat());
        report.setAvgWater(avg.water());

        return reportRepository.save(report);
    }

    private String getWeeklyAnalysisFromAI(User user, ReportAverages avg) {
        String prompt = """
            You are a fitness & nutrition coach.
            Create a weekly evaluation and action plan.

            User profile:
            - Name: %s
            - Age: %s
            - Weight(kg): %s
            - Height(cm): %s
            - Activity level: %s
            - Goal: %s

            Weekly averages (last 7 days):
            - Calories in (kcal): %.2f
            - Calories out (kcal): %.2f
            - Protein (g): %.2f
            - Carbs (g): %.2f
            - Fat (g): %.2f
            - Water: %.2f

            Output requirements:
            1) Short summary (2-3 lines)
            2) What is good (bullets)
            3) What needs improvement (bullets)
            4) Concrete plan for next week (bullets)
            5) One motivational sentence
            """
                .formatted(
                        safe(user.getFullName()),
                        safe(user.getAge()),
                        safe(user.getWeight()),
                        safe(user.getHeight()),
                        safe(user.getActivityLevel()),
                        safe(user.getGoal()),
                        avg.caloriesIn(), avg.caloriesOut(),
                        avg.protein(), avg.carbs(), avg.fat(), avg.water()
                );

        return callAI(prompt);
    }

    private String callAI(String prompt) {
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("model", model);
        requestJson.put("temperature", 0.7);

        // OpenAI-compatible format
        requestJson.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openrouter.ai/api/v1/chat/completions",
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("AI analysis request failed. status=" + response.getStatusCode());
        }

        try {
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");
            return String.valueOf(message.get("content"));
        } catch (Exception e) {
            throw new RuntimeException("AI response parsing error: " + e.getMessage());
        }
    }

    private String safe(Object o) {
        return o == null ? "-" : String.valueOf(o);
    }

    // DTO (record)
    private record ReportAverages(
            double caloriesIn,
            double caloriesOut,
            double protein,
            double carbs,
            double fat,
            double water
    ) {}
}
