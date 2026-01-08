 package com.pix.bip.controller;

import com.pix.bip.model.PixPayment;
import com.pix.bip.service.PixPaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PixPaymentController.class)
class PixPaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PixPaymentService pixPaymentService;

    @Test
    void testGetAllPixPaymentsReturn() throws Exception {
        
        PixPayment firstPayment = new PixPayment();
        firstPayment.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        firstPayment.setSenderPixKey("sender1");
        firstPayment.setReceiverPixKey("receiver1");
        firstPayment.setAmount(new BigDecimal("50.00"));
        firstPayment.setDescription("First transfer");
        firstPayment.setStatus("COMPLETED");

        PixPayment secondPayment = new PixPayment();
        secondPayment.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        secondPayment.setSenderPixKey("sender2");
        secondPayment.setReceiverPixKey("receiver2");
        secondPayment.setAmount(new BigDecimal("75.00"));
        secondPayment.setDescription("Second payment");
        secondPayment.setStatus("COMPLETED");

        List<PixPayment> mockList = Arrays.asList(
            firstPayment,
            secondPayment
        );

        when(pixPaymentService.getAllPixPayments()).thenReturn(mockList);

        mockMvc.perform(get("/pix-payment/payment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$[1].id").value("00000000-0000-0000-0000-000000000002"));
    }
}