package com.pix.bip.dto;

import java.util.UUID;
import com.pix.bip.model.Benefit;
import java.math.BigDecimal;


public class BenefitResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal discountPercentage;



    public BenefitResponse(Benefit benefit) {
        this.id = benefit.getId();
        this.name = benefit.getName();
        this.description = benefit.getDescription();
        this.discountPercentage = benefit.getDiscountPercentage();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}