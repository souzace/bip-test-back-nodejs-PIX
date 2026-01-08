package com.pix.bip.controller;

import com.pix.bip.model.Benefit;
import com.pix.bip.service.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/benefit")
public class BenefitController {

    @Autowired
    private BenefitService benefitService;

    @PostMapping
    public ResponseEntity<Benefit> createBenefit(@RequestBody Benefit benefit) {
        Benefit createdBenefit = benefitService.createBenefit(benefit);
        return ResponseEntity.status(201).body(createdBenefit);
    }

    @GetMapping
    public ResponseEntity<List<Benefit>> getAllBenefits() {
        List<Benefit> benefits = benefitService.getAllBenefits();
        return ResponseEntity.ok(benefits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Benefit> getBenefitById(@PathVariable Long id) {
        return benefitService.getBenefitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        benefitService.deleteBenefit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Benefit> updateBenefit(@PathVariable Long id, @RequestBody Benefit updatedBenefit) {
        Benefit benefit = benefitService.updateBenefit(id, updatedBenefit);
        return ResponseEntity.ok(benefit);
    }
}