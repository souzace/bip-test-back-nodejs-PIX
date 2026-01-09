package com.pix.bip.service;

import com.pix.bip.dto.BenefitRequest;
import com.pix.bip.dto.BenefitResponse;
import com.pix.bip.model.Benefit;
import com.pix.bip.repository.BenefitRepository;
import com.pix.bip.service.BenefitService;
import java.util.UUID;
import java.util.Optional;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BenefitServiceTest {

    @Mock
    private BenefitRepository benefitRepository;

    @InjectMocks
    private BenefitService benefitService;

    @Test
    void testCreateBenefit() {
        BenefitRequest request = new BenefitRequest();
        request.setName("Student Discount");
        request.setDescription("Discount for students");
        request.setDiscountPercentage(new BigDecimal("15.00"));

        Benefit benefit = new Benefit();
        benefit.setName(request.getName());
        benefit.setDescription(request.getDescription());
        benefit.setDiscountPercentage(request.getDiscountPercentage());

        when(benefitRepository.save(any(Benefit.class))).thenReturn(benefit);

        BenefitResponse result = benefitService.createBenefit(request);

        assertNotNull(result);
        assertEquals("Student Discount", result.getName());
        assertEquals("Discount for students", result.getDescription());
        assertEquals(new BigDecimal("15.00"), result.getDiscountPercentage());
    }   

    @Test
    void testGetBenefitById() {
        Benefit benefit = new Benefit();
        benefit.setId(UUID.randomUUID());
        benefit.setName("Senior Citizen Discount");
        benefit.setDescription("Discount for senior citizens");

        when(benefitRepository.findById(benefit.getId())).thenReturn(Optional.of(benefit));
        Optional<BenefitResponse> result = benefitService.getBenefitById(benefit.getId());

        assertTrue(result.isPresent());
        assertEquals("Senior Citizen Discount", result.get().getName());
        assertEquals("Discount for senior citizens", result.get().getDescription());
    }

    @Test
    void testGetAllBenefits() {
        Benefit benefit1 = new Benefit();
        benefit1.setId(UUID.randomUUID());
        benefit1.setName("Employee Discount");
        benefit1.setDescription("Discount for employees");

        Benefit benefit2 = new Benefit();
        benefit2.setId(UUID.randomUUID());
        benefit2.setName("Holiday Discount");
        benefit2.setDescription("Discount during holidays");

        when(benefitRepository.findAll()).thenReturn(java.util.Arrays.asList(benefit1, benefit2));

        java.util.List<BenefitResponse> result = benefitService.getAllBenefits();

        assertEquals(2, result.size());
        assertEquals("Employee Discount", result.get(0).getName());
        assertEquals("Holiday Discount", result.get(1).getName());
    }

    @Test
    void testDeleteBenefit() {
        UUID benefitId = UUID.randomUUID();

        doNothing().when(benefitRepository).deleteById(benefitId);

        benefitService.deleteBenefit(benefitId);

        verify(benefitRepository, times(1)).deleteById(benefitId);
    }

    @Test
    void testUpdateBenefit() {
        Benefit existingBenefit = new Benefit();
        existingBenefit.setId(UUID.randomUUID());
        existingBenefit.setName("Old Name");
        existingBenefit.setDescription("Old Description");

        BenefitRequest updatedRequest = new BenefitRequest();
        updatedRequest.setName("New Name");
        updatedRequest.setDescription("New Description");

        when(benefitRepository.findById(existingBenefit.getId())).thenReturn(Optional.of(existingBenefit));
        when(benefitRepository.save(any(Benefit.class))).thenReturn(existingBenefit);

        Optional<BenefitResponse> result = benefitService.updateBenefit(existingBenefit.getId(), updatedRequest);

        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        assertEquals("New Description", result.get().getDescription());
    }

}