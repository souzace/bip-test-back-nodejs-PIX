package com.pix.bip.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.pix.bip.service.PixTransferService;
import com.pix.bip.model.PixTransfer;

import com.pix.bip.dto.PixTransferRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.pix.bip.dto.PixTransferResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/pix")
public class PixTransferController {

    @Autowired
    private PixTransferService service;

    @Autowired
    private javax.validation.Validator validator;


    @PostMapping("/transfer")
    public ResponseEntity<PixTransferResponse> transferPix(@Valid @RequestBody PixTransferRequest request) {

        Set<ConstraintViolation<PixTransferRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        PixTransfer transfer = service.createPixTransfer(request);

        PixTransferResponse response = new PixTransferResponse(
            String.valueOf(transfer.getId()),
            "Success",
            "Transfer created successfully"
        );
        return ResponseEntity.status(201).body(response);
    }


    @GetMapping("/transfer")
    public ResponseEntity<List<PixTransferResponse>> listTransfers() {
        List<PixTransfer> transfers = service.getAllPixTransfers();
        List<PixTransferResponse> responses = transfers.stream()
        .map(transfer -> 
            new PixTransferResponse(
                String.valueOf(transfer.getId()),
                "Success",
                "Transfer retrieved successfully"
            )
        ).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}