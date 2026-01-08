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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
    public ResponseEntity<Page<PixPaymentResponse>> listPayments(
       @RequestParam(required = false) String status,
       @RequestParam(required = false) String senderPixKey,
       @RequestParam(required = false) String receiverPixKey,
       @RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "10") int size) {   

        Pageable pageable = PageRequest.of(page, size);
        Page<PixPayment> paymentsPage = service.getAllPixPayments(status, senderPixKey, receiverPixKey, pageable);
        Page<PixPaymentResponse> responsePage = paymentsPage.map(payment -> new PixPaymentResponse(payment));
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<PixPaymentResponse> getPixPaymentById(@PathVariable UUID id) {
        return service.getPixPaymentById(id)
                .map(payment -> ResponseEntity.ok(new PixPaymentResponse(payment)))
                .orElse(ResponseEntity.notFound().build());
    }
    
}