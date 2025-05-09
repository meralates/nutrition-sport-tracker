package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.AiRequestData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIRecommendationService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIRecommendationService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getDailyRecommendation(AiRequestData data) {
        String prompt = buildDailyPrompt(data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // JSON Body Hazırlama
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "openai/gpt-3.5-turbo");
        requestBody.put("messages", Collections.singletonList(message));

        try {
            String json = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL, HttpMethod.POST, requestEntity, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                return extractContentFromResponse(responseBody); // Sadece content dön
            } else {
                return "AI isteği başarısız oldu: " + response.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "AI isteği başarısız oldu: " + e.getMessage();
        }
    }

    private String buildDailyPrompt(AiRequestData data) {
        return String.format("""
            Kullanıcı bilgileri:
            - Yaş: %d
            - Cinsiyet: %s
            - Boy: %d cm
            - Kilo: %d kg
            - Aktivite Seviyesi: %s
            - Hedef: %s

            Bugünkü veriler:
            - Alınan kalori: %d kcal
            - Yakılan kalori: %d kcal
            - Protein: %d g
            - Karbonhidrat: %d g
            - Yağ: %d g
            - Su tüketimi: %.1f litre
            - Yapılan egzersiz: %s

            Lütfen yukarıdaki verilere göre sağlıklı yaşam tavsiyesi ver. 
            Beslenme, su tüketimi ve egzersiz hakkında öneriler içersin.
            Cevap Türkçe ve kullanıcı dostu olsun.
            """,
                data.getAge(),
                data.getGender(),
                data.getHeight(),
                data.getWeight(),
                data.getActivityLevel(),
                data.getGoal(),
                data.getDailyCalories(),
                data.getCaloriesBurned(),
                data.getProtein(),
                data.getCarbs(),
                data.getFat(),
                data.getWaterIntake(),
                data.getExerciseDone()
        );
    }

    //JSON'dan sadece "content" çeken yeni method
    private String extractContentFromResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

        if (contentNode.isMissingNode()) {
            throw new Exception("Content bulunamadı!");
        }

        return contentNode.asText();
    }
}
