package com.pix.bip.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "pix_transfers")
public class PixTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_pix_key", nullable = false)
    private String senderPixKey;

    @Column(name = "receiver_pix_key", nullable = false)
    private String receiverPixKey;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
