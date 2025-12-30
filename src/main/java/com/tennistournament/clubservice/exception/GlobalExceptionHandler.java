package com.tennistournament.clubservice.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.tennistournament.clubservice.controller")
public class GlobalExceptionHandler {

    private Map<String, Object> createErrorResponse(HttpStatus status, String message, Map<String, Object> details) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", "N/A"); // Can be obtained from request
        
        if (details != null && !details.isEmpty()) {
            errorResponse.put("details", details);
        }
        
        return errorResponse;
    }

    // Rate Limiting errors (429 Too Many Requests)
    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(RequestNotPermitted ex) {
        Map<String, Object> details = new HashMap<>();
        details.put("availablePermissions", "0");
        details.put("retryAfterSeconds", 60);
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.TOO_MANY_REQUESTS,
            "Rate limit exceeded. Please try again later.",
            details
        );
        
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .header("X-RateLimit-Limit", "100")
            .header("X-RateLimit-Remaining", "0")
            .header("X-RateLimit-Reset", "60")
            .header("Retry-After", "60")
            .body(errorResponse);
    }

    // ResponseStatusException 
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        String reason = ex.getReason() != null ? ex.getReason() : ex.getStatusCode().toString();
        HttpStatus status = ex.getStatusCode() instanceof HttpStatus 
            ? (HttpStatus) ex.getStatusCode() 
            : HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        Map<String, Object> errorResponse = createErrorResponse(
            status,
            reason,
            null
        );
        
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    // Validation errors 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                String errorMessage = error.getDefaultMessage();
                errors.put(error.getObjectName(), errorMessage);
            }
        });
        
        Map<String, Object> details = new HashMap<>();
        details.put("validationErrors", errors);
        details.put("totalErrors", errors.size());
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Validation failed for " + errors.size() + " field(s)",
            details
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // All other exceptions 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        // Log full stacktrace for debugging (but don't expose to client)
        ex.printStackTrace(); // Replace with logger in production
        
        Map<String, Object> details = new HashMap<>();
        details.put("exceptionType", ex.getClass().getSimpleName());
        // In production, don't expose exception details to client!
        // details.put("detailMessage", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please contact support if the problem persists.",
            details
        );
        
        // Remove details in production
        errorResponse.remove("details");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}