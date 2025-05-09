package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.ProductSearchResponse;
import com.example.nutritionsporttracker.service.NutritionixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class NutritionixController {

    private final NutritionixService nutritionixService;

    @Autowired
    public NutritionixController(NutritionixService nutritionixService) {
        this.nutritionixService = nutritionixService;
    }

    @GetMapping("/api/products/search")
    public Mono<ProductSearchResponse> search(@RequestParam String query) {
        return nutritionixService.searchProductByName(query);
    }

}
