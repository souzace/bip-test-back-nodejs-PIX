package com.pix.bip.controller;

import com.pix.bip.dto.PixTransferRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pix")
public class PixTransferController {

    @PostMapping("/transfer")
    public String transferPix(@RequestBody PixTransferRequest request) {
        // Lógica para processar a transferência Pix
        return "Transferência de " + request.getAmount() + " de " + request.getSenderPixKey() +
               " para " + request.getReceiverPixKey() + " realizada com sucesso!";
    }
}