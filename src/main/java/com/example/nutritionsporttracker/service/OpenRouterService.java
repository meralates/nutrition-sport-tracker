package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.ActivityLevel;
import com.example.nutritionsporttracker.model.Goal;
import com.example.nutritionsporttracker.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OpenRouterService {

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public OpenRouterService(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public String generatePersonalizedRecommendation(String email) {
        Optional<User> optionalUser = userService.findByEmail(email);

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
        // Enum'ları doğru şekilde döndür
        String activityLevel = formatEnum(user.getActivityLevel().name(), User.ActivityLevel.class);
        String goal = formatEnum(user.getGoal().name(), User.Goal.class);


        return """
            Benim adım %s. %d yaşındayım, boyum %.1f cm ve kilom %.1f kg. 
            Aktivite seviyem: %s. Hedefim: %s. 
            Bana günlük beslenme ve spor önerileri verir misin?
            """.formatted(user.getFullName(), user.getAge(), user.getHeight(), user.getWeight(), activityLevel, goal);
    }

    // Enum türlerini güvenli şekilde formatla
    private <E extends Enum<E>> String formatEnum(String value, Class<E> enumClass) {
        try {
            // Enum değerine dönüştür ve string olarak döndür
            E enumValue = Enum.valueOf(enumClass, value.toUpperCase());
            return enumValue.toString().replace("_", " ");
        } catch (IllegalArgumentException e) {
            // Eğer geçersiz bir değer gelirse, null döndürebiliriz veya default bir değer dönebiliriz
            return "Geçersiz değer";
        }
    }

    public String sendOpenRouterRequest(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mistral-7b-instruct");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://openrouter.ai/api/v1/chat/completions",
                request,
                String.class
        );

        // 🔍 Burada JSON yanıtı parse ediyoruz
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            String messageContent = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return messageContent;

        } catch (Exception e) {
            e.printStackTrace();
            return "LLM yanıtı işlenemedi.";
        }
    }

}
