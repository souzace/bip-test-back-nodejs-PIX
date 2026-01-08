package com.pix.bip.exception;

import com.pix.bip.dto.PixPaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.MethodArgumentNotValidException;


@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<PixPaymentResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String description = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Invalid Data.");

        PixPaymentResponse response = new PixPaymentResponse(
            "ERROR",
            description
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<PixPaymentResponse> handleDataIntegrityViolationException(Exception ex) {
        PixPaymentResponse response = new PixPaymentResponse(
            "ERROR",
            "Required Fields or Invalid Data: "
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PixPaymentResponse> handleGenericException(Exception ex) {
        PixPaymentResponse response = new PixPaymentResponse(
            "ERROR",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
