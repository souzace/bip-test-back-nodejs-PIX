package com.pix.bip.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.pix.bip.service.PixPaymentService;
import com.pix.bip.model.PixPayment;

import com.pix.bip.dto.PixPaymentRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.pix.bip.dto.PixPaymentResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.UUID;

@RestController
@RequestMapping("/pix-payment")
public class PixPaymentController {

    @Autowired
    private PixPaymentService service;

    @Autowired
    private javax.validation.Validator validator;


    @PostMapping("/payment")
    public ResponseEntity<PixPaymentResponse> makePayment(@Valid @RequestBody PixPaymentRequest request) {
        Set<ConstraintViolation<PixPaymentRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        PixPayment paymentCreated = service.createPixPayment(request);

        PixPaymentResponse response = new PixPaymentResponse(paymentCreated);
        return ResponseEntity.status(201).body(response);
    }


    @GetMapping("/payment")
    public ResponseEntity<List<PixPaymentResponse>> listPayments() {
        List<PixPayment> payments = service.getAllPixPayments();
        List<PixPaymentResponse> responses = payments.stream()
        .map(payment -> 
            new PixPaymentResponse(payment)
        ).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    
}