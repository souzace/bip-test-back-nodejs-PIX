package com.pix.bip.repository;

import com.pix.bip.model.Benefit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitRepository extends JpaRepository<Benefit, UUID> {
}