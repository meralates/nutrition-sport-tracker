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

    @Value("${openrouter.api.key}")
    private String apiKey;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    public Reports generateWeeklyReport(Long userId) {
        User user = getUserById(userId);
        List<Reports> weeklyReports = getReportsFromLastWeek();

        if (weeklyReports.isEmpty()) {
            throw new RuntimeException("No reports found for the past 7 days.");
        }

        ReportAverages averages = calculateAverages(weeklyReports);
        String aiAnalysis = getWeeklyAnalysisFromAI(user, averages);

        return saveReport(user, averages, aiAnalysis);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found."));
    }

    private List<Reports> getReportsFromLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(WEEK_DURATION_DAYS, ChronoUnit.DAYS);
        return reportRepository.findByGeneratedAtAfter(oneWeekAgo);
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
            User: %s, %d years old, %s kg, %s cm.
            Activity level: %s, Goal: %s.
            Last week's nutrition and exercise summary:
            - Calories in: %.2f kcal
            - Calories out: %.2f kcal
            - Protein: %.2f g
            - Carbs: %.2f g
            - Fat: %.2f g
            - Water: %.2f L
            Please analyze the data and suggest improvements.
            """
                .formatted(user.getFullName(), user.getAge(), user.getWeight(), user.getHeight(),
                        user.getActivityLevel(), user.getGoal(),
                        avg.caloriesIn(), avg.caloriesOut(), avg.protein(), avg.carbs(), avg.fat(), avg.water());

        return callAI(prompt);
    }

    private String callAI(String prompt) {
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("model", "mistralai/mistral-7b-instruct");
        requestJson.put("temperature", 0.7);
        requestJson.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openrouter.ai/api/v1/chat/completions",
                HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            } catch (Exception e) {
                return "AI response parsing error.";
            }
        }
        return "AI analysis request failed.";
    }

    public Reports addReport(Reports report) {
        return reportRepository.save(report);
    }

    public List<Reports> getReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }

    /**
     * DTO class to bundle average data
     */
    private record ReportAverages(
            double caloriesIn,
            double caloriesOut,
            double protein,
            double carbs,
            double fat,
            double water
    ) {}
}
