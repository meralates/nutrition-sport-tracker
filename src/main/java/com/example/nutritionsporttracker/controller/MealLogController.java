package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.MealLog;
import com.example.nutritionsporttracker.service.MealLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meal-logs")
public class MealLogController {

    private final MealLogService mealLogService;

    @Autowired
    public MealLogController(MealLogService mealLogService) {
        this.mealLogService = mealLogService;
    }

    // Yeni bir meal log eklemek
    @PostMapping
    public ResponseEntity<MealLog> addMealLog(@RequestBody MealLog mealLog) {
        return new ResponseEntity<>(mealLogService.addMealLog(mealLog), HttpStatus.CREATED);
    }

    // Kullanıcıya ait meal logları listelemek
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealLog>> getMealLogsByUserId(@PathVariable Long userId) {
        List<MealLog> mealLogs = mealLogService.getMealLogsByUserId(userId);
        return mealLogs.isEmpty() 
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            : ResponseEntity.ok(mealLogs);
    }
}
