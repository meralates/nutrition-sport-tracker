package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.Motivation;
import com.example.nutritionsporttracker.service.MotivationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/motivation")
public class MotivationController {

    private final MotivationService motivationService;

    public MotivationController(MotivationService motivationService) {
        this.motivationService = motivationService;
    }

    // GET /api/motivation/random
    @GetMapping("/random")
    public Motivation random() {
        return motivationService.getRandomMotivation();
    }
}