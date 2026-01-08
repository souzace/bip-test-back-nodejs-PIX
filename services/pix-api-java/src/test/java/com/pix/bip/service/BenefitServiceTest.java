package com.pix.bip.service;

import com.pix.bip.model.Benefit;
import com.pix.bip.repository.BenefitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BenefitServiceTest {

    @Mock
    private BenefitRepository benefitRepository;

    @InjectMocks
    private BenefitService benefitService;

    @Test
    void testCreateBenefit() {
        Benefit benefit = new Benefit();
        benefit.setName("Student Discount");
        benefit.setDiscountPercentage(new BigDecimal("15.00"));

        when(benefitRepository.save(benefit)).thenReturn(benefit);

        Benefit result = benefitService.createBenefit(benefit);

        assertNotNull(result);
        assertEquals("Student Discount", result.getName());
        assertEquals(new BigDecimal("15.00"), result.getDiscountPercentage());
    }

    @Test
    void testGetBenefitById() {
        Benefit benefit = new Benefit();
        benefit.setId(1L);
        benefit.setName("Senior Citizen Discount");
        benefit.setDiscountPercentage(new BigDecimal("20.00"));

        when(benefitRepository.findById(1L)).thenReturn(Optional.of(benefit));

        Optional<Benefit> result = benefitService.getBenefitById(1L);

        assertTrue(result.isPresent());
        assertEquals("Senior Citizen Discount", result.get().getName());
        assertEquals(new BigDecimal("20.00"), result.get().getDiscountPercentage());
    }

    @Test
    void testGetAllBenefits() {
        Benefit benefit1 = new Benefit();
        benefit1.setId(1L);
        benefit1.setName("Employee Discount");
        benefit1.setDiscountPercentage(new BigDecimal("10.00"));

        Benefit benefit2 = new Benefit();
        benefit2.setId(2L);
        benefit2.setName("Holiday Discount");
        benefit2.setDiscountPercentage(new BigDecimal("25.00"));

        when(benefitRepository.findAll()).thenReturn(java.util.Arrays.asList(benefit1, benefit2));

        java.util.List<Benefit> result = benefitService.getAllBenefits();

        assertEquals(2, result.size());
    }

}