package com.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 Not Found ────────────────────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ── 409 Conflict ─────────────────────────────────────────────────────────────
    @ExceptionHandler({AlreadyParkedException.class, NotParkedException.class, NoAvailableSpotException.class})
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ── 400 Bad Request ──────────────────────────────────────────────────────────
    @ExceptionHandler({InsufficientPaymentException.class, InvalidDateRangeException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ── 400 Validation (@Valid) ──────────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    // ── 500 Fallback ─────────────────────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    @SuppressWarnings("unused")
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    // ── Helper ───────────────────────────────────────────────────────────────────
    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        return build(status, message, null);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (errors != null) body.put("errors", errors);
        return ResponseEntity.status(status).body(body);
    }
}


