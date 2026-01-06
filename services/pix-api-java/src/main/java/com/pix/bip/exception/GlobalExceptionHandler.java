package com.pix.bip.exception;

import com.pix.bip.dto.PixTransferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.MethodArgumentNotValidException;


@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<PixTransferResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Invalid Data.");

        PixTransferResponse response = new PixTransferResponse(
            null,
            "ERROR",
            message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<PixTransferResponse> handleDataIntegrityViolationException(Exception ex) {
        PixTransferResponse response = new PixTransferResponse(
            null,
            "ERROR",
            "Required Fields or Invalid Data: "
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PixTransferResponse> handleGenericException(Exception ex) {
        PixTransferResponse response = new PixTransferResponse(
            null,
            "ERROR",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
