package com.example.nutritionsporttracker.dto;

import jakarta.validation.constraints.*;

public class WaterIntakeRequest {
    @NotNull
    private Long userId;

    @NotNull @Positive
    private Integer amount; // ml

public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
