package com.pix.bip.controller;

import com.pix.bip.service.PixTransferService;
import com.pix.bip.model.PixTransfer;

import com.pix.bip.dto.PixTransferRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/pix")
public class PixTransferController {

    @Autowired
    private PixTransferService service;

    @PostMapping("/transfer")
    public String transferPix(@RequestBody PixTransferRequest request) {
        PixTransfer transfer = new service.createPixTransfer(request);

        return "Transferência de " + transfer.getAmount() + " de " + transfer.getSenderPixKey() +
               " para " + transfer.getReceiverPixKey() + " realizada com sucesso!";
    }
}