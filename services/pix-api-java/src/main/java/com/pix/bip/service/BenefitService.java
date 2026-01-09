package com.pix.bip.service;

import com.pix.bip.model.Benefit;
import com.pix.bip.repository.BenefitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.pix.bip.dto.BenefitRequest;
import com.pix.bip.dto.BenefitResponse;
import java.util.UUID;

@Service
public class BenefitService {

    @Autowired
    private BenefitRepository benefitRepository;

    public BenefitResponse createBenefit(BenefitRequest request) {
        Benefit benefit = mapToEntity(request);
        Benefit savedBenefit = benefitRepository.save(benefit);
        return new BenefitResponse(savedBenefit);
    }

    public List<BenefitResponse> getAllBenefits() {
        List<Benefit> benefits = benefitRepository.findAll();
        return benefits.stream().map(benefit -> 
            new BenefitResponse(benefit)
        ).toList();
    }

    public Optional<BenefitResponse> getBenefitById(UUID id) {
        return benefitRepository.findById(id)
        .map(benefit -> 
            new BenefitResponse(benefit)
        );
    }

    public boolean deleteBenefit(UUID id) {
        benefitRepository.deleteById(id);
        return true;
    }

    public Optional<BenefitResponse> updateBenefit(UUID id, BenefitRequest updatedBenefit) {
        return benefitRepository.findById(id)
        .map(benefit -> {
            benefit.setName(updatedBenefit.getName());
            benefit.setDescription(updatedBenefit.getDescription());
            benefit.setDiscountPercentage(updatedBenefit.getDiscountPercentage());
            Benefit savedBenefit = benefitRepository.save(benefit);
            return new BenefitResponse(savedBenefit);
        });
    }

    private Benefit mapToEntity(BenefitRequest request) {
        Benefit benefit = new Benefit();
        benefit.setName(request.getName());
        benefit.setDescription(request.getDescription());
        benefit.setDiscountPercentage(request.getDiscountPercentage());
        return benefit;
    }
}