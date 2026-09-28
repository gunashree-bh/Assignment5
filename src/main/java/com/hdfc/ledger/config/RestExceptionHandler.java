package com.hdfc.ledger.config;

import com.hdfc.ledger.dto.ErrorResponse;
import com.hdfc.ledger.exception.ClaimNotFoundException;
import com.hdfc.ledger.exception.DuplicatePolicyException;
import com.hdfc.ledger.exception.InvalidRequestException;
import com.hdfc.ledger.exception.PolicyNotFoundException;
import com.hdfc.ledger.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> fields.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponse(400, "Bad Request", "Validation failed", fields));
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<ErrorResponse> policyNotFound(PolicyNotFoundException ex) {
        return error(404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<ErrorResponse> claimNotFound(ClaimNotFoundException ex) {
        return error(404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(DuplicatePolicyException.class)
    public ResponseEntity<ErrorResponse> duplicate(DuplicatePolicyException ex) {
        return error(409, "Conflict", ex.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> invalid(InvalidRequestException ex) {
        return error(400, "Bad Request", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> error(int status, String error, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status, error, message, Map.of()));
    }
}
