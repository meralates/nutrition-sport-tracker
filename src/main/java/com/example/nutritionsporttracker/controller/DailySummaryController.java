package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.service.DailySummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/summary")
public class DailySummaryController {

    private final DailySummaryService dailySummaryService;

    public DailySummaryController(DailySummaryService dailySummaryService) {
        this.dailySummaryService = dailySummaryService;
    }

    @GetMapping("/daily")
    public ResponseEntity<DailySummaryDto> getDailySummary(
            @RequestParam Long userId,
            @RequestParam String date // yyyy-MM-dd
    ) {
        LocalDate localDate = LocalDate.parse(date);

        return dailySummaryService.getDailySummaryDto(userId, localDate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generate() {
        dailySummaryService.generateDailySummary();
        return ResponseEntity.ok("O günün summary'leri oluşturuldu!");
    }
}
