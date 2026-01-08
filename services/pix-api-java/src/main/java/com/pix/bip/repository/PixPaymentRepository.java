package com.pix.bip.repository;

import com.pix.bip.model.PixPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PixPaymentRepository extends JpaRepository<PixPayment, UUID> {
    Page<PixPayment> findByStatus(String status, Pageable pageable);
    Page<PixPayment> findBySenderPixKey(String senderPixKey, Pageable pageable);
    Page<PixPayment> findByReceiverPixKey(String receiverPixKey, Pageable pageable);
    Page<PixPayment> findByStatusAndSenderPixKey(String status, String senderPixKey, Pageable pageable);
    Page<PixPayment> findByStatusAndReceiverPixKey(String status, String receiverPixKey, Pageable pageable);
    Page<PixPayment> findBySenderPixKeyAndReceiverPixKey(String senderPixKey, String receiverPixKey, Pageable pageable);
    Page<PixPayment> findByStatusAndSenderPixKeyAndReceiverPixKey(String status, String senderPixKey, String receiverPixKey, Pageable pageable);
}