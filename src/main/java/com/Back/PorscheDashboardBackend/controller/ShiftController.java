package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.dao.*;
import com.Back.PorscheDashboardBackend.entity.EndOfShiftKosu;
import com.Back.PorscheDashboardBackend.entity.HourlyKosu;
import com.Back.PorscheDashboardBackend.entity.*;
import com.Back.PorscheDashboardBackend.service.ShiftService;
import com.Back.PorscheDashboardBackend.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/shifts")
@PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
public class ShiftController {

    @Autowired
    private LineDao lineDao;
    @Autowired
    private ProjectDataDao projectDataDao;
    @Autowired
    private SerialDao serialDao;
    @Autowired
    private GroupDao groupDao;

    @Autowired
    private HourlyKosuDao hourlyKosuDao;
    @Autowired
    private EndOfShiftKosuDao endOfShiftKosuDao;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ShiftDao shiftDao;
    @Autowired
    private HourlyKosuController hourlyKosuController;

    @Autowired
    private EndOfShiftStatsDao endOfShiftStatsDao;

    @Autowired
    private TimerService timerService;


    @PostMapping
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        Shift createdShift = shiftService.createNewShift(shift);
        return ResponseEntity.ok(createdShift);
    }

    @GetMapping("/check/{date}/{shift}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<Shift> checkShift(@PathVariable("date") Date date, @PathVariable("shift") String shift) {
        Shift existingShift = shiftDao.findByDateAndShift(date, shift);
        if (existingShift != null) {
            return ResponseEntity.ok(existingShift);
        } else {
            return ResponseEntity.ok(null); // Return null when the shift does not exist
        }
    }


        @PutMapping("/finish/{date}/{shift}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<?> finishShift(@PathVariable("date") Date date, @PathVariable("shift") String shift) {
        Shift existingShift = shiftDao.findByDateAndShift(date, shift);
        if (existingShift != null) {
            existingShift.setFinished(true);
            shiftDao.save(existingShift);

            // Fetch hourly Kosu entries for the finished shift using HourlyKosuDao
            List<HourlyKosu> hourlyKosuEntries = hourlyKosuDao.getHourlyKosuFromShift(existingShift);

            // Aggregate data by line, serial, and group
            Map<String, Map<String, Map<String, List<HourlyKosu>>>> lineSerialGroupMap = new HashMap<>();
            for (HourlyKosu entry : hourlyKosuEntries) {
                if (entry.getLine() == null || entry.getSerial() == null || entry.getGroupe() == null) {
                    // Skip this entry if Line, Serial, or Group is null
                    continue;
                }
                String lineName = entry.getLine().getLineName();
                String serialName = entry.getSerial().getSerialName();
                String groupName = entry.getGroupe().getGroupName();

                lineSerialGroupMap.putIfAbsent(lineName, new HashMap<>());
                lineSerialGroupMap.get(lineName).putIfAbsent(serialName, new HashMap<>());
                lineSerialGroupMap.get(lineName).get(serialName).putIfAbsent(groupName, new ArrayList<>());
                lineSerialGroupMap.get(lineName).get(serialName).get(groupName).add(entry);
            }

            // Create and save EndOfShiftKosu entries
            for (Map.Entry<String, Map<String, Map<String, List<HourlyKosu>>>> lineEntry : lineSerialGroupMap.entrySet()) {
                String lineName = lineEntry.getKey();
                Map<String, Map<String, List<HourlyKosu>>> serialGroupMap = lineEntry.getValue();
                for (Map.Entry<String, Map<String, List<HourlyKosu>>> serialEntry : serialGroupMap.entrySet()) {
                    String serialName = serialEntry.getKey();
                    Map<String, List<HourlyKosu>> groupMap = serialEntry.getValue();
                    for (Map.Entry<String, List<HourlyKosu>> groupEntry : groupMap.entrySet()) {
                        String groupName = groupEntry.getKey();
                        List<HourlyKosu> entries = groupEntry.getValue();
                        Line line = lineDao.findByLineName(lineName);
                        Serial serial = serialDao.findBySerialName(serialName);
                        Groupe group = groupDao.findByGroupName(groupName);

                        int Workforce = 0;
                        if (!entries.isEmpty()) {
                            HourlyKosu lastEntry = entries.get(entries.size() - 1); // Get the last entry
                            Workforce = lastEntry.getWorkforce();
                        }

                        // Calculate aggregated values
                        int totalProducedCables = entries.stream().mapToInt(HourlyKosu::getProducedCables).sum();
                        int totalNokCables = entries.stream().mapToInt(HourlyKosu::getNokCables).sum();
                        int totalNetWorkTime = entries.stream().mapToInt(HourlyKosu::getNetWorkTime).sum();
                        double totalMudas = entries.stream().mapToDouble(HourlyKosu::getMudas).sum();
                        double averageKosuEquivalent = entries.stream().mapToDouble(HourlyKosu::getKosuEquivalent).average().orElse(0);

                        // Create and save EndOfShiftKosu entry
                        EndOfShiftKosu endOfShiftKosu = new EndOfShiftKosu();
                        endOfShiftKosu.setShift(existingShift);
                        endOfShiftKosu.setLine(line);
                        endOfShiftKosu.setSerial(serial);
                        endOfShiftKosu.setGroupe(group);
                        endOfShiftKosu.setTotalProducedCables(totalProducedCables);
                        endOfShiftKosu.setTotalNokCables(totalNokCables);
                        endOfShiftKosu.setTotalNetWorkTime(totalNetWorkTime);
                        endOfShiftKosu.setTotalMudas(totalMudas);
                        endOfShiftKosu.setWorkforce(Workforce);
                        endOfShiftKosu.setAverageKosuEquivalent(averageKosuEquivalent);
                        endOfShiftKosuDao.save(endOfShiftKosu);
                    }
                }
            }
            java.sql.Date shiftDate = existingShift.getDate();

// Convert java.sql.Date to LocalDate
            LocalDate localDate = shiftDate.toLocalDate();

// Define start and end of the range for the given date
            LocalDateTime startDateTime = localDate.atStartOfDay();
            LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);
            // Fetch and select the ProjectData entries for the same date as the shift
            List<ProjectData> projectDataEntries = projectDataDao.findByDateBetween(startDateTime, endDateTime);

            for (ProjectData projectData : projectDataEntries) {
                // Convert LocalDateTime to java.sql.Date
                java.sql.Date projectDate = java.sql.Date.valueOf(projectData.getDate().toLocalDate());

                // Compare java.sql.Date of ProjectData with java.sql.Date of Shift
                if (projectDate.equals(existingShift.getDate())) {
                    EndOfShiftStats endOfShiftStats = new EndOfShiftStats();
                    endOfShiftStats.setShift(existingShift);
                    endOfShiftStats.setProject(projectData.getProjectName());
                    endOfShiftStats.setWorkforcePresent(projectData.getWorkforcePresent());
                    endOfShiftStats.setEfficiency(projectData.getEfficiency());
                    endOfShiftStats.setProducedNumber(projectData.getProducedNumber());
                    endOfShiftStats.setNokNumber(projectData.getNokNumber());
                    endOfShiftStats.setDate(projectData.getDate());

                    // Save EndOfShiftStats entity
                    endOfShiftStatsDao.save(endOfShiftStats);

                }}


            System.out.println("Clearing Timers..");
            timerService.clearUserTimers();

            return ResponseEntity.ok("Shift finished successfully.");
        } else {
            return ResponseEntity.ok("Problem with saving the shift");
        }
    }


    // Method to convert LocalDateTime to Date
    public static Date convertToDate(LocalDateTime localDateTime) {
        return Date.valueOf(localDateTime.toLocalDate());
    }

    // Method to convert Date to LocalDateTime
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toLocalDate().atStartOfDay();
    }
    // You can add more endpoints as needed
}
