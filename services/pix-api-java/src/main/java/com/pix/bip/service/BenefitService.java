package com.pix.bip.service;

import com.pix.bip.model.Benefit;
import com.pix.bip.repository.BenefitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BenefitService {

    @Autowired
    private BenefitRepository benefitRepository;

    public Benefit createBenefit(Benefit benefit) {
        return benefitRepository.save(benefit);
    }

    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

    public Optional<Benefit> getBenefitById(Long id) {
        return benefitRepository.findById(id);
    }

    public void deleteBenefit(Long id) {
        benefitRepository.deleteById(id);
    }

    public Benefit updateBenefit(Long id, Benefit updatedBenefit) {
        return benefitRepository.findById(id)
        .map(benefit -> {
            benefit.setName(updatedBenefit.getName());
            benefit.setDiscountPercentage(updatedBenefit.getDiscountPercentage());
            return benefitRepository.save(benefit);
        }).orElseThrow(() -> new RuntimeException("Benefit not found with id " + id));
    }
}