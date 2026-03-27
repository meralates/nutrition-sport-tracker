package com.example.nutritionsporttracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UsdaFoodService {

    @Value("${usda.api.key}")
    private String apiKey;

    @Value("${usda.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<JsonNode> searchFoods(String query) {
        String normalizedQuery = normalize(query);
        String encodedQuery = URLEncoder.encode(normalizedQuery, StandardCharsets.UTF_8);

        String url = baseUrl + "/v1/foods/search"
                + "?api_key=" + apiKey
                + "&query=" + encodedQuery
                + "&pageSize=15"
                + "&dataType=Foundation,SR Legacy,Branded";

        JsonNode response = restTemplate.getForObject(url, JsonNode.class);

        if (response == null || !response.has("foods") || !response.get("foods").isArray()) {
            return List.of();
        }

        List<JsonNode> results = new ArrayList<>();
        for (JsonNode food : response.get("foods")) {
            if (food == null) continue;

            String description = normalize(food.path("description").asText(""));
            String dataType = food.path("dataType").asText("");

            if (description.isBlank()) continue;

            int score = calculateScore(normalizedQuery, description, dataType, food);
            if (score < 0) continue;

            results.add(food);
        }

        results.sort(Comparator.comparingInt(
                food -> -calculateScore(
                        normalizedQuery,
                        normalize(food.path("description").asText("")),
                        food.path("dataType").asText(""),
                        food
                )
        ));

        return results;
    }

    public JsonNode searchBestFood(String query) {
        List<JsonNode> foods = searchFoods(query);
        return foods.isEmpty() ? null : foods.get(0);
    }

    private int calculateScore(String query, String description, String dataType, JsonNode food) {
        int score = 0;

        if (description.equals(query)) {
            score += 100;
        } else if (description.startsWith(query)) {
            score += 70;
        } else if (description.contains(query)) {
            score += 40;
        }

        if ("Foundation".equalsIgnoreCase(dataType)) {
            score += 25;
        } else if ("SR Legacy".equalsIgnoreCase(dataType)) {
            score += 15;
        } else if ("Branded".equalsIgnoreCase(dataType)) {
            score -= 5;
        }

        if (containsBadKeyword(description)) {
            score -= 80;
        }

        double calories = extractNutrient(food, "Energy", "Energy (Atwater General)");
        if (calories > 0) {
            score += 10;
        } else {
            score -= 20;
        }

        return score;
    }

    private boolean containsBadKeyword(String description) {
        return description.contains("dressing")
                || description.contains("sauce")
                || description.contains("gravy")
                || description.contains("dip")
                || description.contains("spread")
                || description.contains("topping");
    }

    public double extractNutrient(JsonNode food, String... nutrientNames) {
        JsonNode nutrients = food.get("foodNutrients");
        if (nutrients == null || !nutrients.isArray()) {
            return 0;
        }

        for (JsonNode nutrient : nutrients) {
            if (nutrient == null) continue;

            String nutrientName = nutrient.path("nutrientName").asText("");
            for (String target : nutrientNames) {
                if (target.equalsIgnoreCase(nutrientName)) {
                    return nutrient.path("value").asDouble(0);
                }
            }
        }

        return 0;
    }

    private String normalize(String text) {
        if (text == null) return "";

        return text
                .trim()
                .toLowerCase()
                .replace("ç", "c")
                .replace("ğ", "g")
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ü", "u");
    }
}