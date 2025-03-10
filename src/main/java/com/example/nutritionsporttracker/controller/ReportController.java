package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Kullanıcı için rapor ekleme
    @PostMapping
    public ResponseEntity<Reports> createReport(@RequestBody Reports report) {
        return new ResponseEntity<>(reportService.addReport(report), HttpStatus.CREATED);
    }

    // Kullanıcı ID'sine göre tüm raporları listeleme
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reports>> getReportsByUserId(@PathVariable Long userId) {
        List<Reports> reports = reportService.getReportsByUserId(userId);
        if (reports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reports);
    }
}
