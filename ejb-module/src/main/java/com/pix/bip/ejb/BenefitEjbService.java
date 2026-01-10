package com.pix.bip.ejb;

import com.pix.bip.dto.PaymentRequest;
import jakarta.ejb.Local;

@Local
public interface BenefitEjbService {
    String processPayment(PaymentRequest paymentRequest);
    Long getBalance(String pixKey);
    void resetBalances();
}
