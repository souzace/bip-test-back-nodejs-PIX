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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}