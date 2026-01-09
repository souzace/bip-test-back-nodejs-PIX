package com.pix.bip.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Benefit {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID  id;

    private String name;
    private BigDecimal discountPercentage;
    private String description;

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

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getDescription() { 
        return this.description; 
    }
}