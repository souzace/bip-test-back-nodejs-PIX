package com.pix.bip.ejb;

import com.pix.bip.dto.PaymentRequest;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Stateless
public class BenefitEjbServiceImpl implements BenefitEjbService {
    
    private static final Logger log = LoggerFactory.getLogger(BenefitEjbServiceImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final ConcurrentHashMap<String, Long> balances = new ConcurrentHashMap<>();
    private static final Object balanceLock = new Object();
    
    static {
        balances.put("11111111-1111-1111-1111-111111111111", 100000L);
        balances.put("22222222-2222-2222-2222-222222222222", 50000L);
    }
    
    @Override
    public String processPayment(PaymentRequest request) {
        validateRequest(request);
        
        log.info("Payment: {} -> {} = R$ {}", request.getPixKey(), request.getReceiverKey(), request.getAmount());
        
        Long amountInCents = request.getAmount().multiply(BigDecimal.valueOf(100)).longValue();
        
        synchronized (balanceLock) {
            Long currentBalance = getBalance(request.getPixKey());
            
            log.debug("Balance: R$ {}", currentBalance / 100.0);
            
            if (currentBalance < amountInCents) {
                throw new IllegalStateException("Insufficient balance");
            }
            
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            
            Long newSenderBalance = currentBalance - amountInCents;
            balances.put(request.getPixKey(), newSenderBalance);
            
            Long receiverBalance = balances.getOrDefault(request.getReceiverKey(), 0L);
            balances.put(request.getReceiverKey(), receiverBalance + amountInCents);
        }
        
        return UUID.randomUUID().toString();
    }
    
    @Override
    public Long getBalance(String pixKey) {
        return balances.getOrDefault(pixKey, 0L);
    }
    
    @Override
    public void resetBalances() {
        synchronized (balanceLock) {
            balances.clear();
            balances.put("11111111-1111-1111-1111-111111111111", 100000L);
            balances.put("22222222-2222-2222-2222-222222222222", 50000L);
        }
    }
    
    private void validateRequest(PaymentRequest request) {
        if (request == null) throw new IllegalArgumentException("Payment request cannot be null");
        if (request.getRequestId() == null || request.getRequestId().isBlank()) throw new IllegalArgumentException("Request ID is required");
        if (request.getPixKey() == null || request.getPixKey().isBlank()) throw new IllegalArgumentException("Sender PIX key is required");
        if (request.getReceiverKey() == null || request.getReceiverKey().isBlank()) throw new IllegalArgumentException("Receiver PIX key is required");
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (request.getPixKey().equals(request.getReceiverKey())) throw new IllegalArgumentException("Sender and receiver cannot be the same");
        if (!balances.containsKey(request.getPixKey())) throw new IllegalArgumentException("Sender not found");
    }
}
