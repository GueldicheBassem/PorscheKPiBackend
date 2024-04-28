package com.Back.PorscheDashboardBackend.controller;

import java.sql.SQLException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof SQLException && "23000".equals(((SQLException) cause).getSQLState())) {
            // Customize the error message based on your application's needs
            String errorMessage = "Error: Duplicate entry violates unique constraint.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        // Handle other types of DataIntegrityViolationException or SQLException
        return new ResponseEntity<>("Error: Data integrity violation.", HttpStatus.BAD_REQUEST);
    }
}

