package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.WaterIntakeRequest;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.model.WaterIntake;
import com.example.nutritionsporttracker.security.CustomUserDetails;
import com.example.nutritionsporttracker.service.WaterIntakeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/water-intake")
@Validated
public class WaterIntakeController {

    private final WaterIntakeService waterIntakeService;

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

        return new ResponseEntity<>(waterIntakeService.addWaterIntake(waterIntake), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WaterIntake>> getWaterIntakesByUserId(@PathVariable Long userId) {
        List<WaterIntake> waterIntakes = waterIntakeService.getWaterIntakesByUserId(userId);
        return waterIntakes.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(waterIntakes);
    }

    @GetMapping("/summary")
    public ResponseEntity<Integer> getDailyWaterSummary(
            @RequestParam Long userId,
            @RequestParam String date
    ) {
        int totalMl = waterIntakeService.calculateDailyWaterIntake(userId, LocalDate.parse(date));
        return ResponseEntity.ok(totalMl);
    }

    @DeleteMapping("/{waterIntakeId}")
    public ResponseEntity<Void> deleteWaterIntake(
            @PathVariable Long waterIntakeId,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        waterIntakeService.deleteWaterIntake(waterIntakeId, me.getId());
        return ResponseEntity.noContent().build();
    }
}