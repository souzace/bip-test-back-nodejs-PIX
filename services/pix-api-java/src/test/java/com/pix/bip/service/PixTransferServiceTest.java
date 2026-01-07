package com.pix.bip.service;

import com.pix.bip.dto.PixTransferRequest;
import com.pix.bip.model.PixTransfer;
import com.pix.bip.repository.PixTransferRepository;
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

@ExtendWith(MockitoExtension.class)
class PixTransferServiceTest {

    @Mock
    private PixTransferRepository pixTransferRepository;

    @InjectMocks
    private PixTransferService pixTransferService;

    @Test
    void testCreatePixTransfer() {
        PixTransferRequest request = new PixTransferRequest();
        request.setSenderPixKey("sender-key");
        request.setReceiverPixKey("receiver-key");
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test transfer");

        PixTransfer savedTransfer = new PixTransfer();
        savedTransfer.setId(1L);
        savedTransfer.setSenderPixKey(request.getSenderPixKey());
        savedTransfer.setReceiverPixKey(request.getReceiverPixKey());
        savedTransfer.setAmount(request.getAmount());
        savedTransfer.setDescription(request.getDescription());

        when(pixTransferRepository.save(any(PixTransfer.class))).thenReturn(savedTransfer);

        PixTransfer result = pixTransferService.createPixTransfer(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("sender-key", result.getSenderPixKey());
        assertEquals("receiver-key", result.getReceiverPixKey());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals("Test transfer", result.getDescription());
    }

    @Test
    void testGetAllPixTransfers() {
        PixTransfer transfer1 = new PixTransfer();
        transfer1.setId(1L);
        transfer1.setSenderPixKey("sender1");
        transfer1.setReceiverPixKey("receiver1");
        transfer1.setAmount(new BigDecimal("50.00"));
        transfer1.setDescription("First transfer");

        PixTransfer transfer2 = new PixTransfer();
        transfer2.setId(2L);
        transfer2.setSenderPixKey("sender2");
        transfer2.setReceiverPixKey("receiver2");
        transfer2.setAmount(new BigDecimal("75.00"));
        transfer2.setDescription("Second transfer");

        when(pixTransferRepository.findAll()).thenReturn(java.util.List.of(transfer1, transfer2));

        java.util.List<PixTransfer> result = pixTransferService.getAllPixTransfers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("sender1", result.get(0).getSenderPixKey());
        assertEquals("sender2", result.get(1).getSenderPixKey());
    }

    @Test
    void testGetAllPixTransfersEmptyResult() {
        when(pixTransferRepository.findAll()).thenReturn(java.util.List.of());

        java.util.List<PixTransfer> result = pixTransferService.getAllPixTransfers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreatePixTransferSenderPixKeyNull() {
        PixTransferRequest request = new PixTransferRequest();
        request.setSenderPixKey(null); 
        request.setReceiverPixKey("user2@email.com");
        request.setAmount(new BigDecimal("100.50"));
        request.setDescription("Teste erro senderPixKey null");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PixTransferRequest>> violations = validator.validate(request);

         assertFalse(violations.isEmpty());
    }

    @Test
    void testCreatePixTransferReceiverPixKeyBlank() {
        PixTransferRequest request = new PixTransferRequest();
        request.setSenderPixKey("user1@email.com");
        request.setReceiverPixKey(""); 
        request.setAmount(new BigDecimal("100.50"));
        request.setDescription("Teste erro receiverPixKey blank");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PixTransferRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreatePixTransferAmountNull() {
        PixTransferRequest request = new PixTransferRequest();
        request.setSenderPixKey("user1@email.com");
        request.setReceiverPixKey("user2@email.com");
        request.setAmount(null);
        request.setDescription("Teste erro amount null");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PixTransferRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}   