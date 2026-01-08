package com.pix.bip.repository;

import com.pix.bip.model.PixPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PixPaymentRepository extends JpaRepository<PixPayment, UUID> {
}