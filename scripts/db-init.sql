-- ============================================================
-- PIX Payment System - Database Schema
-- ============================================================
-- This script creates the necessary tables for the PIX
-- payment system and benefits management.
-- 
-- Prerequisites: PostgreSQL 12+
-- Execute as: psql -U postgres -d pixdb -f db-init.sql
-- ============================================================

-- Benefits Table
-- Stores benefit/discount information in the system
CREATE TABLE IF NOT EXISTS benefit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    discount_percentage NUMERIC(5, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_discount_positive CHECK (discount_percentage >= 0),
    CONSTRAINT chk_discount_max CHECK (discount_percentage <= 100),
    CONSTRAINT chk_name_not_empty CHECK (length(name) > 0)
);

-- Indexes for query optimization
CREATE INDEX IF NOT EXISTS idx_benefit_name ON benefit(name);
CREATE INDEX IF NOT EXISTS idx_benefit_created_at ON benefit(created_at);

-- PIX Payments Table
-- Records all transfers made in the system
CREATE TABLE IF NOT EXISTS pix_payment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    sender_pix_key VARCHAR(100) NOT NULL,
    receiver_pix_key VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_status_valid CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    CONSTRAINT chk_different_keys CHECK (sender_pix_key != receiver_pix_key)
);

-- Indexes for query optimization and filters
CREATE INDEX IF NOT EXISTS idx_pix_payment_sender ON pix_payment(sender_pix_key);
CREATE INDEX IF NOT EXISTS idx_pix_payment_receiver ON pix_payment(receiver_pix_key);
CREATE INDEX IF NOT EXISTS idx_pix_payment_status ON pix_payment(status);
CREATE INDEX IF NOT EXISTS idx_pix_payment_created_at ON pix_payment(created_at);
CREATE INDEX IF NOT EXISTS idx_pix_payment_sender_created ON pix_payment(sender_pix_key, created_at DESC);

-- Table and column comments
COMMENT ON TABLE benefit IS 'Stores user benefits and balance information';
COMMENT ON COLUMN benefit.balance IS 'Available balance in Brazilian Reais (R$)';
COMMENT ON COLUMN benefit.pix_key IS 'Unique PIX key of the user (CPF, email, phone, etc)';

COMMENT ON TABLE pix_payment IS 'Record of all PIX transactions performed';
COMMENT ON COLUMN pix_payment.amount IS 'Transfer amount in Brazilian Reais (R$)';
COMMENT ON COLUMN pix_payment.status IS 'Transaction status: PENDING, COMPLETED, FAILED, CANCELLED';
COMMENT ON COLUMN pix_payment.sender_pix_key IS 'PIX key of the sender';
COMMENT ON COLUMN pix_payment.receiver_pix_key IS 'PIX key of the receiver';

-- ============================================================
-- End of Schema
-- ============================================================
