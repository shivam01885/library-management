package com.library.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Converts expected application errors into consistent JSON responses.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Returns a 404 response when a requested resource is missing.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException exception) {
        return createResponse(HttpStatus.NOT_FOUND, exception.getMessage(), new ArrayList<String>());
    }

    // Returns a 409 response for duplicate resources and unavailable books.
    @ExceptionHandler({DuplicateResourceException.class, BookNotAvailableException.class})
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException exception) {
        return createResponse(HttpStatus.CONFLICT, exception.getMessage(), new ArrayList<String>());
    }

    // Returns field-level messages when a request DTO fails validation.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<String>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getField() + ": " + error.getDefaultMessage()));
        return createResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    // Returns a clear 400 response when request JSON cannot be read.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, "Request body is missing or invalid", new ArrayList<String>());
    }

    // Returns a 409 response when database constraints prevent a write or deletion.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataConflict(DataIntegrityViolationException exception) {
        return createResponse(HttpStatus.CONFLICT,
                "The record conflicts with existing data or is still in use", new ArrayList<String>());
    }

    // Builds the shared timestamp, status, message, and errors response shape.
    private ResponseEntity<Map<String, Object>> createResponse(
            HttpStatus status, String message, List<String> errors) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("timestamp", Instant.now().toString());
        response.put("status", status.value());
        response.put("message", message);
        response.put("errors", errors);
        return ResponseEntity.status(status).body(response);
    }
}
