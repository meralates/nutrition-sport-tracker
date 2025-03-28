package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecommendationService {

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public RecommendationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    public String generateDailyRecommendation(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "Kullanıcı bulunamadı.";
        }

        User user = optionalUser.get();
        String userPrompt = generateUserPrompt(user);

        try {
            return sendOpenRouterRequest(userPrompt);
        } catch (Exception e) {
            return "Öneri oluşturulurken hata oluştu: " + e.getMessage();
        }
    }

    private String generateUserPrompt(User user) {
        return """
            Benim adım %s. %d yaşındayım, boyum %.1f cm ve kilom %.1f kg.
            Aktivite seviyem: %s. Hedefim: %s.
            Bugünkü beslenme ve antrenman önerilerini alabilir miyim?
            """.formatted(user.getFullName(), user.getAge(), user.getHeight(), user.getWeight(),
                user.getActivityLevel(), user.getGoal());
    }

    private String sendOpenRouterRequest(String userMessage) throws Exception {
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("model", "mistralai/mistral-7b-instruct");
        requestJson.put("temperature", 0.7);

        Map<String, String> userMessageObj = new HashMap<>();
        userMessageObj.put("role", "user");
        userMessageObj.put("content", userMessage);

        requestJson.put("messages", List.of(userMessageObj));

        String jsonBody = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("API hatası: " + response.getStatusCode());
        }
    }
}
