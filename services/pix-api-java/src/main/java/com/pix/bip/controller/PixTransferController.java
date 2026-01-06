package com.pix.bip.controller;

import com.pix.bip.service.PixTransferService;
import com.pix.bip.model.PixTransfer;

import com.pix.bip.dto.PixTransferRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.pix.bip.dto.PixTransferResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/pix")
public class PixTransferController {

    @Autowired
    private PixTransferService service;

    @PostMapping("/transfer")
    public ResponseEntity<PixTransferResponse> transferPix(@Valid @RequestBody PixTransferRequest request) {
        PixTransfer transfer = service.createPixTransfer(request);

        PixTransferResponse response = new PixTransferResponse(
            String.valueOf(transfer.getId()),
            "Success",
            "Transfer created successfully"
        );
        return ResponseEntity.status(201).body(response);
    }
}