package com.Back.PorscheDashboardBackend.controller;


import com.Back.PorscheDashboardBackend.entity.Serial;
import com.Back.PorscheDashboardBackend.service.SerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/serial")
@PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
public class SerialController {

    @Autowired
    private SerialService serialService;

    @PostMapping
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Serial> createNewSerial(@RequestBody Serial serial) {
        Serial createdSerial = serialService.createNewSerial(serial);
        return new ResponseEntity<>(createdSerial, HttpStatus.CREATED);
    }

    @GetMapping("/{serialId}")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<Serial> getSerialById(@PathVariable Long serialId) {
        Optional<Serial> serialOptional = serialService.getSerialById(serialId);
        return serialOptional.map(serial -> new ResponseEntity<>(serial, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Serial>> getAllSerials() {
        List<Serial> serials = serialService.getAllSerials();
        return new ResponseEntity<>(serials, HttpStatus.OK);
    }
    @PutMapping("/{serialId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Serial> updateSerial(@PathVariable Long serialId, @RequestBody Serial updatedSerial) {
        Optional<Serial> existingSerialOptional = serialService.getSerialById(serialId);
        if (existingSerialOptional.isPresent()) {
            Serial existingSerial = existingSerialOptional.get();
            // Update the existing serial with the new values
            existingSerial.setSerialName(updatedSerial.getSerialName());
            existingSerial.setReference(updatedSerial.getReference());
            existingSerial.setCoefficient(updatedSerial.getCoefficient());
            existingSerial.setProject(updatedSerial.getProject()); // Set the project

            // Optionally, update other properties as needed

            // Save the updated serial
            Serial savedSerial = serialService.updateSerial(existingSerial);
            return new ResponseEntity<>(savedSerial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{serialId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Void> deleteSerial(@PathVariable Long serialId) {
        serialService.deleteSerial(serialId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

