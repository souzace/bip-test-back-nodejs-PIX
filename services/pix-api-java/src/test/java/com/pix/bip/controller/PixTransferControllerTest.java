 package com.pix.bip.controller;

import com.pix.bip.model.PixTransfer;
import com.pix.bip.service.PixTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PixTransferController.class)
class PixTransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PixTransferService pixTransferService;

    @Test
    void testGetAllPixTransfersReturn() throws Exception {
        
        PixTransfer firstTransfer = new PixTransfer();
        firstTransfer.setId(1L);
        firstTransfer.setSenderPixKey("sender1");
        firstTransfer.setReceiverPixKey("receiver1");
        firstTransfer.setAmount(new BigDecimal("50.00"));
        firstTransfer.setDescription("First transfer");

        PixTransfer secondTransfer = new PixTransfer();
        secondTransfer.setId(2L);
        secondTransfer.setSenderPixKey("sender2");
        secondTransfer.setReceiverPixKey("receiver2");
        secondTransfer.setAmount(new BigDecimal("75.00"));
        secondTransfer.setDescription("Second transfer");

        List<PixTransfer> mockList = Arrays.asList(
            firstTransfer,
            secondTransfer
        );

        when(pixTransferService.getAllPixTransfers()).thenReturn(mockList);

        mockMvc.perform(get("/pix/transfer"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].transactionId").value("1"))
            .andExpect(jsonPath("$[1].transactionId").value("2"));
    }
}