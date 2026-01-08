package com.pix.bip.service;

import java.util.List;

import com.pix.bip.dto.PixPaymentRequest;
import com.pix.bip.model.PixPayment;
import com.pix.bip.repository.PixPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PixPaymentService {

    @Autowired
    private PixPaymentRepository pixPaymentRepository;

    public PixPayment createPixPayment(PixPaymentRequest request) {
        PixPayment payment = mapToEntity(request);
        return pixPaymentRepository.save(payment);
    }

    public Page<PixPayment> getAllPixPayments(String status, String senderPixKey, String receiverPixKey, Pageable pageable) {
        if (status != null && senderPixKey != null && receiverPixKey != null) {
            return pixPaymentRepository.findByStatusAndSenderPixKeyAndReceiverPixKey(status, senderPixKey, receiverPixKey, pageable);
        } else if (status != null && senderPixKey != null) {
            return pixPaymentRepository.findByStatusAndSenderPixKey(status, senderPixKey, pageable);
        } else if (status != null && receiverPixKey != null) {
            return pixPaymentRepository.findByStatusAndReceiverPixKey(status, receiverPixKey, pageable);
        } else if (senderPixKey != null && receiverPixKey != null) {
            return pixPaymentRepository.findBySenderPixKeyAndReceiverPixKey(senderPixKey, receiverPixKey, pageable);
        } else if (status != null) {
            return pixPaymentRepository.findByStatus(status, pageable);
        } else if (senderPixKey != null) {
            return pixPaymentRepository.findBySenderPixKey(senderPixKey, pageable);
        } else if (receiverPixKey != null) {
            return pixPaymentRepository.findByReceiverPixKey(receiverPixKey, pageable);
        } else {
            return pixPaymentRepository.findAll(pageable);
        }
    }

    public Optional<PixPayment> getPixPaymentById(UUID id) {
        return pixPaymentRepository.findById(id);
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