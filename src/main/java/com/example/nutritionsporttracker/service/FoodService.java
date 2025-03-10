package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.FoodProduct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class FoodService {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v0/product/";

    public Optional<FoodProduct> getFoodProduct(String productName) {
        RestTemplate restTemplate = new RestTemplate();
        String formattedUrl = API_URL + productName + ".json";

        try {
            FoodProduct product = restTemplate.getForObject(formattedUrl, FoodProduct.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}