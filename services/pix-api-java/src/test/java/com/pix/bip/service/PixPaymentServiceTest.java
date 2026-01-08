package com.pix.bip.service;

import com.pix.bip.dto.PixPaymentRequest;
import com.pix.bip.model.PixPayment;
import com.pix.bip.repository.PixPaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PixPaymentServiceTest {
    @Mock
    private PixPaymentRepository pixPaymentRepository;

    @InjectMocks
    private PixPaymentService pixPaymentService;

    @Test
    void testCreatePixPayment() {
        PixPaymentRequest request = new PixPaymentRequest();
        request.setSenderPixKey("sender-key");
        request.setReceiverPixKey("receiver-key");
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test payment");
        request.setStatus("PENDING");

        PixPayment savedPayment = new PixPayment();
        savedPayment.setId(UUID.randomUUID());
        savedPayment.setSenderPixKey(request.getSenderPixKey());
        savedPayment.setReceiverPixKey(request.getReceiverPixKey());
        savedPayment.setAmount(request.getAmount());
        savedPayment.setDescription(request.getDescription());
        savedPayment.setStatus(request.getStatus());

        when(pixPaymentRepository.save(any(PixPayment.class))).thenReturn(savedPayment);

        PixPayment result = pixPaymentService.createPixPayment(request);

        assertNotNull(result);
        assertEquals("sender-key", result.getSenderPixKey());
        assertEquals("receiver-key", result.getReceiverPixKey());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals("Test payment", result.getDescription());
        assertEquals("PENDING", result.getStatus());

    }

    @Test
    void testGetAllPixPayments() {
        PixPayment payment1 = new PixPayment();
        payment1.setId(UUID.randomUUID());
        payment1.setSenderPixKey("sender1");
        payment1.setReceiverPixKey("receiver1");
        payment1.setAmount(new BigDecimal("50.00"));
        payment1.setDescription("First payment");
        payment1.setStatus("COMPLETED");

        PixPayment payment2 = new PixPayment();
        payment2.setId(UUID.randomUUID());
        payment2.setSenderPixKey("sender2");
        payment2.setReceiverPixKey("receiver2");
        payment2.setAmount(new BigDecimal("75.00"));
        payment2.setDescription("Second payment");
        payment2.setStatus("COMPLETED");

        when(pixPaymentRepository.findAll()).thenReturn(java.util.List.of(payment1, payment2));

        java.util.List<PixPayment> result = pixPaymentService.getAllPixPayments();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("sender1", result.get(0).getSenderPixKey());
        assertEquals("sender2", result.get(1).getSenderPixKey());
    }

    @Test
    void testGetAllPixPaymentsEmptyResult() {
        when(pixPaymentRepository.findAll()).thenReturn(java.util.List.of());

        java.util.List<PixPayment> result = pixPaymentService.getAllPixPayments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreatePixPaymentSenderPixKeyNull() {
        PixPaymentRequest request = new PixPaymentRequest();
        request.setSenderPixKey(null); 
        request.setReceiverPixKey("user2@email.com");
        request.setAmount(new BigDecimal("100.50"));
        request.setDescription("Teste erro senderPixKey null");
        request.setStatus("PENDING");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PixPaymentRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreatePixPaymentReceiverPixKeyBlank() {
        PixPaymentRequest request = new PixPaymentRequest();
        request.setSenderPixKey("user1@email.com");
        request.setReceiverPixKey(""); 
        request.setAmount(new BigDecimal("100.50"));
        request.setDescription("Teste erro receiverPixKey blank");
        request.setStatus("PENDING");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PixPaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreatePixPaymentAmountNull() {
        PixPaymentRequest request = new PixPaymentRequest();
        request.setSenderPixKey("user1@email.com");
        request.setReceiverPixKey("user2@email.com");
        request.setAmount(null);
        request.setDescription("Teste erro amount null");
        request.setStatus("PENDING");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PixPaymentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}   