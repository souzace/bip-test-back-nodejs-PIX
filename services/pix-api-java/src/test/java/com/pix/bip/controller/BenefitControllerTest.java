package com.pix.bip.controller;

import com.pix.bip.dto.BenefitRequest;
import com.pix.bip.dto.BenefitResponse;
import com.pix.bip.model.Benefit;
import com.pix.bip.service.BenefitService;
import java.util.UUID;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

@WebMvcTest(BenefitController.class)
class BenefitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private BenefitService benefitService;


    @Test
    void testCreateBenefit() throws Exception {
        BenefitRequest benefitRequest = new BenefitRequest();
        benefitRequest.setName("Health Insurance");
        benefitRequest.setDescription("Comprehensive health coverage");
        benefitRequest.setDiscountPercentage(new java.math.BigDecimal("20.00"));

        Benefit createdBenefit = new Benefit();
        createdBenefit.setId(UUID.randomUUID());
        createdBenefit.setName(benefitRequest.getName());
        createdBenefit.setDescription(benefitRequest.getDescription());
        createdBenefit.setDiscountPercentage(benefitRequest.getDiscountPercentage());
        
        when(benefitService.createBenefit(any(BenefitRequest.class))).thenReturn(new BenefitResponse(createdBenefit));

        mockMvc.perform(post("/api/v1/benefits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdBenefit.getId().toString()))
                .andExpect(jsonPath("$.name").value("Health Insurance"))
                .andExpect(jsonPath("$.description").value("Comprehensive health coverage"))
                .andExpect(jsonPath("$.discountPercentage").value(20.00));

        
    }
}