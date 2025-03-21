package com.example.nutritionsporttracker.dto;

import java.util.List;

public class ProductSearchResponse {
    private List<FoodItem> common;

    public List<FoodItem> getCommon() {
        return common;
    }

    public void setCommon(List<FoodItem> common) {
        this.common = common;
    }
}
