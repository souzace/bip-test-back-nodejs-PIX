package com.pix.bip.service;

import com.pix.bip.dto.PixTransferRequest;
import com.pix.bip.model.PixTransfer;
import com.pix.bip.repository.PixTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PixTransferService {

    @Autowired
    private final PixTransferRepository pixTransferRepository;

    public PixTransfer createPixTransfer(PixTransferRequest request) {
        PixTransfer pixTransfer = new PixTransfer();
        pixTransfer.setSenderPixKey(request.getSenderPixKey());
        pixTransfer.setReceiverPixKey(request.getReceiverPixKey());
        pixTransfer.setAmount(request.getAmount());
        pixTransfer.setDescription(request.getDescription());

        return pixTransferRepository.save(pixTransfer);
    }
}