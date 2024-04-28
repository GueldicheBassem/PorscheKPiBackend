package com.Back.PorscheDashboardBackend.controller;
import com.Back.PorscheDashboardBackend.service.ShiftService;
import com.Back.PorscheDashboardBackend.service.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Back.PorscheDashboardBackend.dao.*;
import com.Back.PorscheDashboardBackend.entity.*;
import com.Back.PorscheDashboardBackend.service.HourlyKosuService;
import com.Back.PorscheDashboardBackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class HourlyKosuController {
    @Autowired
    private HourlyKosuService hourlyKosuService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SerialDao serialDao;
    @Autowired
    private ShiftDao shiftDao;
    @Autowired
    private HourlyKosuDao hourlyKosuDao;

    @Autowired
    ShiftService shiftService;

    @Autowired
    private EndOfShiftStatsDao endOfShiftStatsDao;

    @Autowired
    private EndOfShiftKosuDao endOfShiftKosuDao;

    @GetMapping("/groupes")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Groupe>> getGroups() {
        List<Groupe> groupes = groupDao.findAll();
        return new ResponseEntity<>(groupes, HttpStatus.OK);
    }

    @GetMapping("/lines")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Line>> getAllLines() {
        List<Line> lines = lineDao.findAll();
        return new ResponseEntity<>(lines, HttpStatus.OK);
    }

    @GetMapping("/serials/{lineId}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Serial>> getSerialsByLine(@PathVariable Long lineId) {
        List<Serial> serials = serialDao.findByProject_Line_LineId(lineId);
        return new ResponseEntity<>(serials, HttpStatus.OK);
    }

    @Autowired
    private TimerService timerService;


        @PostMapping("/hourly-kosu")
        @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
        public ResponseEntity createHourlyKosu(@RequestBody HourlyKosuRequest hourlyKosuRequest, @RequestParam double kosuobjectif)
        {
            Shift shift = shiftDao.findByDateAndShift(hourlyKosuRequest.getShiftId().getDate(), hourlyKosuRequest.getShiftId().getShift());
            if (shift.isFinished()) {
                // If the shift is finished, return an error response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot create hourly kosu for a finished shift");
            }

            HourlyKosu hourlyKosu = new HourlyKosu();

            hourlyKosu.setShift(shiftDao.findByDateAndShift((hourlyKosuRequest.getShiftId().getDate()), hourlyKosuRequest.getShiftId().getShift()));
            hourlyKosu.setLine(lineDao.findById(hourlyKosuRequest.getLineId()).orElse(null));
            hourlyKosu.setGroupe(groupDao.findById(hourlyKosuRequest.getGroupId()).orElse(null));
            hourlyKosu.setSerial(serialDao.findById(hourlyKosuRequest.getSerialId()).orElse(null));
            hourlyKosu.setNetWorkTime(hourlyKosuRequest.getNetWorkTime());
            hourlyKosu.setProducedCables(hourlyKosuRequest.getProducedCables());
            hourlyKosu.setNokCables(hourlyKosuRequest.getNokCables());
            hourlyKosu.setWorkforce(hourlyKosuRequest.getWorkforce());
            hourlyKosu.setHour(hourlyKosuRequest.getHour());

            // Calculate mudas and KosuEquivalent
            double kosuEquivalent = calculateKosuEquivalent(hourlyKosu);



            // Set the calculated values
            hourlyKosu.setKosuEquivalent(kosuEquivalent);
            hourlyKosu.setMudas (((kosuEquivalent-kosuobjectif)*hourlyKosuRequest.getProducedCables())/3600);




            HourlyKosu createdHourlyKosu = hourlyKosuService.createHourlyKosu(hourlyKosu);
            return ResponseEntity.ok(createdHourlyKosu);
        }


    @GetMapping("/hourly-kosu/{date}/{shift}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<HourlyKosuResponse>> getHourlyKosuEntries(@PathVariable("date") Date date, @PathVariable("shift") String shift) {
        Shift existingShift = shiftDao.findByDateAndShift(date, shift);

        List<HourlyKosu> hourlyKosuEntries = hourlyKosuDao.getHourlyKosuFromShift(existingShift);
        List<HourlyKosuResponse> responses = hourlyKosuService.mapHourlyKosuToResponse(hourlyKosuEntries);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/end-of-kosu/{date}/{shift}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<List<EndOfShiftKosu>> getEndOfKosuEntries(@PathVariable("date") Date date, @PathVariable("shift") String shift) {
        Shift existingShift = shiftDao.findByDateAndShift(date, shift);

        List<EndOfShiftKosu> endOfKosuEntries = endOfShiftKosuDao.findByShift(existingShift);
        return ResponseEntity.ok(endOfKosuEntries);
    }


    @GetMapping("/end-of-shift-stats/{date}/{shift}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser'  )")
    public ResponseEntity<List<EndOfShiftStats>> getEndOfShiftStatsEntries(@PathVariable("date") Date date, @PathVariable("shift") String shift) {
        Shift existingShift = shiftDao.findByDateAndShift(date, shift);

        List<EndOfShiftStats> endOfShiftStatsEntries = endOfShiftStatsDao.findByShift(existingShift);
        return ResponseEntity.ok(endOfShiftStatsEntries);
    }

    // Endpoint to modify an hourly Kosu entry

    // Add logger declaration at the top of your class
    private static final Logger logger = LoggerFactory.getLogger(HourlyKosuController.class);

    @PutMapping("/hourly-kosu/{id}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<HourlyKosu> modifyHourlyKosu(@PathVariable Long id, @RequestBody HourlyKosuRequest modifiedHourlyKosu,@RequestParam double kosuobjectifedit) {
        logger.info("Received request to modify HourlyKosu with ID: {}", id);

        // Retrieve the existing entry by its ID
        Optional<HourlyKosu> optionalHourlyKosu = hourlyKosuDao.findByEntryId(id);

        if (optionalHourlyKosu.isEmpty()) {
            logger.warn("HourlyKosu with ID {} not found", id);
            return ResponseEntity.notFound().build(); // Entry not found
        }

        HourlyKosu existingHourlyKosu = optionalHourlyKosu.get();
        logger.info("Found HourlyKosu with ID: {}", existingHourlyKosu.getEntryId());

        // Update the existing entry with the modified data
        existingHourlyKosu.setLine(lineDao.findById(modifiedHourlyKosu.getLineId()).orElse(null));
        existingHourlyKosu.setGroupe(groupDao.findById(modifiedHourlyKosu.getGroupId()).orElse(null));
        existingHourlyKosu.setSerial(serialDao.findById(modifiedHourlyKosu.getSerialId()).orElse(null));
        existingHourlyKosu.setNetWorkTime(modifiedHourlyKosu.getNetWorkTime());
        existingHourlyKosu.setProducedCables(modifiedHourlyKosu.getProducedCables());
        existingHourlyKosu.setNokCables(modifiedHourlyKosu.getNokCables());
        existingHourlyKosu.setWorkforce(modifiedHourlyKosu.getWorkforce());
        existingHourlyKosu.setHour(modifiedHourlyKosu.getHour());


        // Recalculate KosuEquivalent and Mudas if necessary
        double kosuEquivalent = calculateKosuEquivalent(existingHourlyKosu);

        existingHourlyKosu.setKosuEquivalent(kosuEquivalent);
        existingHourlyKosu.setMudas(((kosuEquivalent-kosuobjectifedit)*modifiedHourlyKosu.getProducedCables())/3600);


        // Save the modified entry
        hourlyKosuDao.save(existingHourlyKosu);

        logger.info("Successfully modified HourlyKosu with ID: {}", existingHourlyKosu.getEntryId());

        return ResponseEntity.ok(existingHourlyKosu);
    }

    @PostMapping("/hourly-kosu/timer/start")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<Map<String, Object>> startTimer(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Date date, @RequestParam String shift) {
        // Fetch the shift details from the database based on date and shift parameters
        Shift shifte = shiftDao.findByDateAndShift(date, shift);
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null && shift != null) {
            if (shifte.isFinished()) {
                // If the shift has ended, include a message in the response
                System.out.println("Shift has ened");
                response.put("message", "Shift"+shifte+"has ended");
                return ResponseEntity.ok(response);
            } else {
                String username = userDetails.getUsername();
                LocalDateTime expirationTime = timerService.getExpirationTime(username);
                if (expirationTime == null) {
                    // If the user doesn't exist in userTimers and shift is not finished, start a new timer
                    timerService.startTimer(username);
                    expirationTime = timerService.getExpirationTime(username); // Get the new expiration time
                    System.out.println("Timer has been started for user: " + username);
                } else {
                    System.out.println("User already has an active timer");
                }

                timerService.logUserTimers();
                // Include the expiration time in the response
                response.put("expirationTime", expirationTime);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }




    @PostMapping("/hourly-kosu/timer/check")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser')")
    public ResponseEntity<Map<String, Object>> checkTimer(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestParam(value = "manual", required = false) boolean manualCheck) {
        Map<String, Object> response = new HashMap<>();

        if (userDetails != null) {
            String username = userDetails.getUsername();

            if (timerService.isTimerExpired(username)) {
                if (!manualCheck) {
                    // Timer expired with automatic check
                    System.out.println("Timer expired. User Should be penalized.");

                    // No need to start the timer here, it will be started automatically
                } else {
                    System.out.println("Manual check and timer has expired, reset the timer without penalizing the user");
                    timerService.resetTimer(username);
                }
            } else {
                if (manualCheck) {
                    System.out.println("Manual check and timer has not expired, reset the timer");
                    timerService.resetTimer(username);
                }
            }

            // Get the expiration time and add it to the response
            LocalDateTime expirationTime = timerService.getExpirationTime(username);
            response.put("expirationTime", expirationTime);
        }

        return ResponseEntity.ok(response);
    }






    @GetMapping("/hourly-kosu/{id}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<HourlyKosu> getHourlyKosuById(@PathVariable Long id) {
        // Retrieve the HourlyKosu entry by its ID
        Optional<HourlyKosu> optionalHourlyKosu = hourlyKosuDao.findByEntryId(id);

        // Check if the HourlyKosu entry exists
        if (optionalHourlyKosu.isPresent()) {
            // If the entry exists, return it in the response
            HourlyKosu hourlyKosu = optionalHourlyKosu.get();
            return ResponseEntity.ok(hourlyKosu);
        } else {
            // If the entry does not exist, return a 404 Not Found status
            return ResponseEntity.notFound().build();
        }
    }


    // Method to calculate mudas


    // Method to calculate KosuEquivalent
    public double calculateKosuEquivalent(HourlyKosu hourlyKosu) {
        return (hourlyKosu.getWorkforce() * hourlyKosu.getNetWorkTime()) /
                (hourlyKosu.getProducedCables() * hourlyKosu.getSerial().getCoefficient());
    }

}
