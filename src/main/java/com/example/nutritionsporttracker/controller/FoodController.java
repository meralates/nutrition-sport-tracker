package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.FoodProduct;
import com.example.nutritionsporttracker.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    
    @Autowired
    private FoodService foodService;

    @GetMapping("/search")
    public Optional<FoodProduct> searchFood(@RequestParam String productName) {
        return foodService.getFoodProduct(productName);
    }
}