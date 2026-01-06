package com.pix.bip.repository;

import com.pix.bip.model.PixTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PixTransferRepository extends JpaRepository<PixTransfer, Long> {
}