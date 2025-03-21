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
    private final RestTemplate restTemplate;

    @Value("${openrouter.api.key}")
    private String apiKey;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    public Reports generateWeeklyReport(Long userId) {
        User user = getUserById(userId);
        List<Reports> weeklyReports = getLastWeekReports();
        validateWeeklyData(weeklyReports);

        String aiAnalysis = getWeeklyAnalysisFromAI(user, weeklyReports);

        return saveWeeklyReport(user, weeklyReports, aiAnalysis);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
    }

    private List<Reports> getLastWeekReports() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(WEEK_DURATION_DAYS, ChronoUnit.DAYS);
        return reportRepository.findByGeneratedAtAfter(oneWeekAgo);
    }

    private void validateWeeklyData(List<Reports> weeklyReports) {
        if (weeklyReports.isEmpty()) {
            throw new RuntimeException("Geçen hafta için yeterli veri bulunamadı.");
        }
    }

    private Reports saveWeeklyReport(User user, List<Reports> weeklyReports, String aiAnalysis) {
        Reports newReport = new Reports();
        newReport.setUser(user);
        newReport.setGeneratedAt(LocalDateTime.now());
        newReport.setReportText(aiAnalysis);
        newReport.setCaloriesIn(calculateAverage(weeklyReports, Reports::getCaloriesIn));
        newReport.setCaloriesOut(calculateAverage(weeklyReports, Reports::getCaloriesOut));
        newReport.setAvgProtein(calculateAverage(weeklyReports, Reports::getAvgProtein));
        newReport.setAvgCarbs(calculateAverage(weeklyReports, Reports::getAvgCarbs));
        newReport.setAvgFat(calculateAverage(weeklyReports, Reports::getAvgFat));
        newReport.setAvgWater(calculateAverage(weeklyReports, Reports::getAvgWater));

        return reportRepository.save(newReport);
    }

    private double calculateAverage(List<Reports> reports, ToDoubleFunction<Reports> getter) {
        return reports.stream().mapToDouble(getter).average().orElse(0);
    }

    private String getWeeklyAnalysisFromAI(User user, List<Reports> weeklyReports) {
        String prompt = createPromptForAI(user, weeklyReports);
        return sendOpenRouterRequest(prompt);
    }

    private String createPromptForAI(User user, List<Reports> weeklyReports) {
        return """
            Kullanıcı: %s, %d yaşında, %s kg, %s cm.
            Aktivite seviyesi: %s, Hedef: %s.
            Geçen haftaki beslenme ve egzersiz verileri:
            - Ortalama alınan kalori: %.2f kcal
            - Ortalama yakılan kalori: %.2f kcal
            - Ortalama protein: %.2f g
            - Ortalama karbonhidrat: %.2f g
            - Ortalama yağ: %.2f g
            - Ortalama su tüketimi: %.2f L
            Kullanıcı için haftalık sağlık analizini yap ve önerilerde bulun.
            """
                .formatted(user.getFullName(), user.getAge(), user.getWeight(), user.getHeight(),
                        user.getActivityLevel(), user.getGoal(),
                        calculateAverage(weeklyReports, Reports::getCaloriesIn),
                        calculateAverage(weeklyReports, Reports::getCaloriesOut),
                        calculateAverage(weeklyReports, Reports::getAvgProtein),
                        calculateAverage(weeklyReports, Reports::getAvgCarbs),
                        calculateAverage(weeklyReports, Reports::getAvgFat),
                        calculateAverage(weeklyReports, Reports::getAvgWater));
    }

    private String sendOpenRouterRequest(String userMessage) {
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("model", "mistralai/mistral-7b-instruct");
        requestJson.put("temperature", 0.7);

        Map<String, String> userMessageObj = new HashMap<>();
        userMessageObj.put("role", "user");
        userMessageObj.put("content", userMessage);

        requestJson.put("messages", List.of(userMessageObj));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openrouter.ai/api/v1/chat/completions",
                HttpMethod.POST, entity, String.class);

        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : "LLM API hatası!";
    }

    public Reports addReport(Reports report) {
        return reportRepository.save(report);
    }

    public List<Reports> getReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }
}
