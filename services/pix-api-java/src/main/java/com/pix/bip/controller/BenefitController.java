package com.pix.bip.controller;

import com.pix.bip.model.Benefit;
import com.pix.bip.service.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.pix.bip.dto.BenefitRequest;
import com.pix.bip.dto.BenefitResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/benefits")
public class BenefitController {

    @Autowired
    private BenefitService benefitService;

    @PostMapping
    public ResponseEntity<BenefitResponse> createBenefit(@RequestBody BenefitRequest benefitRequest) {
        BenefitResponse createdBenefit = benefitService.createBenefit(benefitRequest);
        return ResponseEntity.status(201).body(createdBenefit);
    }

    @GetMapping
    public ResponseEntity<List<BenefitResponse>> getAllBenefits() {
        List<BenefitResponse> benefits = benefitService.getAllBenefits();
        return ResponseEntity.ok(benefits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BenefitResponse> getBenefitById(@PathVariable UUID id) {
        return benefitService.getBenefitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable UUID id) {
        benefitService.deleteBenefit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BenefitResponse> updateBenefit(@PathVariable UUID id, @RequestBody BenefitRequest updatedRequest) {
        return benefitService.updateBenefit(id, updatedRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}