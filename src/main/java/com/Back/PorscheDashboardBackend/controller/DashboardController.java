package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.entity.EndOfShiftKosu;
import com.Back.PorscheDashboardBackend.entity.ProcessedEndOfShiftStats;
import com.Back.PorscheDashboardBackend.service.EndOfShiftKosuService;
import com.Back.PorscheDashboardBackend.service.EndOfShiftStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final EndOfShiftStatsService endOfShiftStatsService;
    private final EndOfShiftKosuService endOfShiftKosuService;

    @Autowired
    public DashboardController(EndOfShiftStatsService endOfShiftStatsService, EndOfShiftKosuService endOfShiftKosuService) {
        this.endOfShiftStatsService = endOfShiftStatsService;
        this.endOfShiftKosuService = endOfShiftKosuService;
    }

    @GetMapping("/last7days")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<ProcessedEndOfShiftStats>> getLast7DaysEfficiency() {
        List<ProcessedEndOfShiftStats> efficiencyStats = endOfShiftStatsService.getLast7DaysEfficiency();
        return new ResponseEntity<>(efficiencyStats, HttpStatus.OK);
    }


    @GetMapping("/last7dayskosu")
    public ResponseEntity<Map<String, Map<String, Map<String, Double>>>> getLast7DaysKosu() {
        List<EndOfShiftKosu> kosuList = endOfShiftKosuService.getLast7DaysKosu();
        Map<String, Map<String, Map<String, Double>>> kosuMap = endOfShiftKosuService.processKosuData(kosuList);
        return new ResponseEntity<>(kosuMap, HttpStatus.OK);
    }
}
