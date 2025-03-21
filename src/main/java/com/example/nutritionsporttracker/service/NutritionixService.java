package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.dto.ProductSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NutritionixService {

    @Value("${nutritionix.api.key}")
    private String apiKey;

    @Value("${nutritionix.app.id}")
    private String appId;

    private final WebClient webClient;

    public NutritionixService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://trackapi.nutritionix.com/v2").build();
    }

    public Mono<ProductSearchResponse> searchProductByName(String productName) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search/instant")
                        .queryParam("query", productName)
                        .build())
                .header("x-app-id", appId)
                .header("x-app-key", apiKey)
                .retrieve()
                .bodyToMono(ProductSearchResponse.class);
    }
}
