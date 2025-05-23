package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.WaterIntakeRequest;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.model.WaterIntake;
import com.example.nutritionsporttracker.service.WaterIntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/water-intake")
public class WaterIntakeController {

    private final WaterIntakeService waterIntakeService;

    @Autowired
    public WaterIntakeController(WaterIntakeService waterIntakeService) {
        this.waterIntakeService = waterIntakeService;
    }

    @PostMapping
    public ResponseEntity<WaterIntake> addWaterIntake(@RequestBody WaterIntakeRequest request) {
        WaterIntake waterIntake = new WaterIntake();
        User user = new User();
        user.setId(request.getUserId());

        waterIntake.setUser(user);
        waterIntake.setAmountMl(request.getAmount());
        waterIntake.setCreatedAt(LocalDateTime.now());

        return new ResponseEntity<>(waterIntakeService.addWaterIntake(waterIntake), HttpStatus.CREATED);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WaterIntake>> getWaterIntakesByUserId(@PathVariable Long userId) {
        List<WaterIntake> waterIntakes = waterIntakeService.getWaterIntakesByUserId(userId);
        if (waterIntakes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(waterIntakes);
    }
}
