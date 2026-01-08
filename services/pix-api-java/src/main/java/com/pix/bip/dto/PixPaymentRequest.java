package com.pix.bip.dto;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class PixPaymentRequest {
    @NotBlank
    private String senderPixKey;

    @NotBlank
    private String receiverPixKey;

    @NotNull
    private BigDecimal amount;
    
    private String description;

    @NotBlank
    private String status; 

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

    
}