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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;
import org.mockito.Mockito;

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

        when(pixPaymentService.getAllPixPayments(null, null, null, PageRequest.of(0, 10)))
        .thenReturn(new PageImpl<>(mockList));

        mockMvc.perform(get("/api/v1/pix/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$.content[1].id").value("00000000-0000-0000-0000-000000000002"));
    }

    @Test
    void testGetPaymentByIdFound() throws Exception {
        UUID paymentId = UUID.fromString("00000000-0000-0000-0000-000000000003");

        PixPayment payment = new PixPayment();
        payment.setId(paymentId);
        payment.setSenderPixKey("sender3");
        payment.setReceiverPixKey("receiver3");
        payment.setAmount(new BigDecimal("150.00"));
        payment.setDescription("Third payment");
        payment.setStatus("PENDING");

        when(pixPaymentService.getPixPaymentById(paymentId)).thenReturn(java.util.Optional.of(payment));

        mockMvc.perform(get("/api/v1/pix/payments/{id}", paymentId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000003"))
            .andExpect(jsonPath("$.senderPixKey").value("sender3"));
    }

    @Test
    void testGetPaymentByIdNotFound() throws Exception {
        UUID paymentId = UUID.fromString("00000000-0000-0000-0000-000000000004");

        when(pixPaymentService.getPixPaymentById(paymentId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/pix/payments/{id}", paymentId))
            .andExpect(status().isNotFound());
    }

    @Test
    void testListPaymentsWithPaginationAndFilter() throws Exception {
       UUID paymentId = UUID.randomUUID();
       PixPayment payment = new PixPayment();
       payment.setId(paymentId);
       payment.setSenderPixKey("sender-key");
       payment.setReceiverPixKey("receiver-key");
       payment.setAmount(new BigDecimal("150.00"));
       payment.setDescription("Test payment");
       payment.setStatus("COMPLETED");

       Page<PixPayment> pagedResponse = new PageImpl<>(Collections.singletonList(payment));
       Mockito.when(pixPaymentService.getAllPixPayments(
           "COMPLETED",
           null,
           null, 
           PageRequest.of(0, 10)
       )).thenReturn(pagedResponse);

       mockMvc.perform(get("/api/v1/pix/payments")
           .param("status", "COMPLETED")
           .param("page", "0")
           .param("size", "10"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content[0].id").value(paymentId.toString()))
           .andExpect(jsonPath("$.content[0].status").value("COMPLETED"))
           .andExpect(jsonPath("$.content[0].amount").value(150.00));
       
    }
}