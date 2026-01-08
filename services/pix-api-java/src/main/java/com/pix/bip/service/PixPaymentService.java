package com.pix.bip.service;

import java.util.List;

import com.pix.bip.dto.PixPaymentRequest;
import com.pix.bip.model.PixPayment;
import com.pix.bip.repository.PixPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PixPaymentService {

    @Autowired
    private PixPaymentRepository pixPaymentRepository;

    public PixPayment createPixPayment(PixPaymentRequest request) {
        PixPayment payment = mapToEntity(request);
        return pixPaymentRepository.save(payment);
    }

    public List<PixPayment> getAllPixPayments() {
        return pixPaymentRepository.findAll();
    }

    private PixPayment mapToEntity(PixPaymentRequest request) {
        PixPayment payment = new PixPayment();
        payment.setSenderPixKey(request.getSenderPixKey());
        payment.setReceiverPixKey(request.getReceiverPixKey());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setStatus(request.getStatus());
        return payment;
    }

}