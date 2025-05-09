package com.example.nutritionsporttracker.dto;

import java.util.List;

public class ProductSearchResponse {
    private List<FoodItem> foods;

    public List<FoodItem> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodItem> foods) {
        this.foods = foods;
    }
}
