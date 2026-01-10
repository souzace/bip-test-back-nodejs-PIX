package com.pix.bip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pixKey;
    private String receiverKey;
    private BigDecimal amount;
    private String description;
    private String requestId;
}
