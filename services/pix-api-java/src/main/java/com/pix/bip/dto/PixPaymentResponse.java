package com.pix.bip.dto;

import com.pix.bip.model.PixPayment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PixPaymentResponse {
    
    private UUID id;
    private String senderPixKey;
    private String receiverPixKey;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSenderPixKey() {
        return senderPixKey;
    }

    public void setSenderPixKey(String senderPixKey) {
        this.senderPixKey = senderPixKey;
    }

    public String getReceiverPixKey() {
        return receiverPixKey;
    }

    public void setReceiverPixKey(String receiverPixKey) {
        this.receiverPixKey = receiverPixKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PixPaymentResponse(String status, String description) {
        this.id = null;
        this.senderPixKey = null;
        this.receiverPixKey = null;
        this.amount = null;
        this.description = description;
        this.status = status;
        this.createdAt = null;
    }

    public PixPaymentResponse(PixPayment payment) {
        this.id = payment.getId();
        this.senderPixKey = payment.getSenderPixKey();
        this.receiverPixKey = payment.getReceiverPixKey();
        this.amount = payment.getAmount();
        this.description = payment.getDescription();
        this.status = payment.getStatus();
        this.createdAt = payment.getCreatedAt();
    }
}