package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Reports> createReport(@RequestBody Reports report) {
        return ResponseEntity.ok(reportService.addReport(report));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reports>> getReportsByUserId(@PathVariable Long userId) {
        List<Reports> reports = reportService.getReportsByUserId(userId);
        return reports.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(reports);
    }

    @GetMapping("/weekly")
    public ResponseEntity<Reports> getWeeklyReport(@RequestParam Long userId) {
        Reports weeklyReport = reportService.generateWeeklyReport(userId);
        return ResponseEntity.ok(weeklyReport);
    }
}
