package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.AiRequestData;
import com.example.nutritionsporttracker.util.PromptBuilder;
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
        String prompt = PromptBuilder.buildDailyPrompt(data);  // Prompt oluşturucu dışarı alındı

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
                return extractContentFromResponse(responseBody);
            } else {
                return "AI isteği başarısız oldu: " + response.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "AI isteği başarısız oldu: " + e.getMessage();
        }
    }

    private String extractContentFromResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

        if (contentNode.isMissingNode()) {
            throw new Exception("Content bulunamadı!");
        }

        return contentNode.asText();
    }
}
