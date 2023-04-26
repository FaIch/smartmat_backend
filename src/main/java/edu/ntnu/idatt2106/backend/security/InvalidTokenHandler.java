package edu.ntnu.idatt2106.backend.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class InvalidTokenHandler {


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());

        // Replace HttpStatus.valueOf(600) with the desired custom error code
        return ResponseEntity.status(HttpStatus.valueOf(600)).body(response);
    }
}
