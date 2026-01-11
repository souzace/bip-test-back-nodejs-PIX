-- ============================================================
-- PIX Payment System - Seed Data
-- ============================================================
-- This script populates the database with initial data for
-- development and testing.
-- 
-- Prerequisites: db-init.sql already executed
-- Execute as: psql -U postgres -d pixdb -f seed.sql
-- ============================================================

-- Clear existing data (development only)
TRUNCATE TABLE pix_payment CASCADE;
TRUNCATE TABLE benefit CASCADE;

-- ============================================================
-- Insert Benefits (Users with balance)
-- ============================================================

INSERT INTO benefit (id, user_id, balance, pix_key, created_at, updated_at) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'user001', 1000.00, '11987654321', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440002', 'user002', 500.00, '11987654322', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440003', 'user003', 750.50, '11987654323', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440004', 'user004', 250.00, 'maria@email.com', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440005', 'user005', 1500.75, 'joao@email.com', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440006', 'user006', 100.00, '12345678901', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440007', 'user007', 2000.00, '98765432100', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440008', 'user008', 50.00, 'pedro@email.com', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440009', 'user009', 800.25, 'ana@email.com', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440010', 'user010', 300.00, '11999887766', NOW(), NOW());

-- ============================================================
-- Insert PIX Payments (Transaction history)
-- ============================================================

INSERT INTO pix_payment (id, amount, status, sender_pix_key, receiver_pix_key, description, created_at, updated_at) VALUES
    (gen_random_uuid(), 100.00, 'COMPLETED', '11987654321', '11987654322', 'Rent payment', NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days'),
    (gen_random_uuid(), 50.00, 'COMPLETED', '11987654322', '11987654323', 'Bill split', NOW() - INTERVAL '9 days', NOW() - INTERVAL '9 days'),
    (gen_random_uuid(), 200.00, 'COMPLETED', '11987654323', 'maria@email.com', 'Birthday gift', NOW() - INTERVAL '8 days', NOW() - INTERVAL '8 days'),
    (gen_random_uuid(), 75.50, 'COMPLETED', 'maria@email.com', 'joao@email.com', 'Service payment', NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days'),
    (gen_random_uuid(), 150.00, 'COMPLETED', 'joao@email.com', '12345678901', 'Product purchase', NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days'),
    (gen_random_uuid(), 300.00, 'COMPLETED', '12345678901', '98765432100', 'Freelance payment', NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days'),
    (gen_random_uuid(), 25.00, 'COMPLETED', '98765432100', 'pedro@email.com', 'Snack', NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days'),
    (gen_random_uuid(), 500.00, 'COMPLETED', 'pedro@email.com', 'ana@email.com', 'Loan', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),
    (gen_random_uuid(), 120.00, 'COMPLETED', 'ana@email.com', '11999887766', 'Course payment', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'),
    (gen_random_uuid(), 80.00, 'COMPLETED', '11999887766', '11987654321', 'Refund', NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days'),
    (gen_random_uuid(), 45.00, 'PENDING', '11987654321', 'joao@email.com', 'Pending payment', NOW(), NOW()),
    (gen_random_uuid(), 15.00, 'FAILED', 'pedro@email.com', 'maria@email.com', 'Processing failure', NOW() - INTERVAL '5 hours', NOW() - INTERVAL '5 hours');

-- ============================================================
-- Verification of inserted data
-- ============================================================

-- Count inserted records
SELECT 
    (SELECT COUNT(*) FROM benefit) as total_benefits,
    (SELECT COUNT(*) FROM pix_payment) as total_payments;

-- Balance summary
SELECT 
    'Total System Balance' as description,
    SUM(balance) as total_amount 
FROM benefit;

-- Transaction summary by status
SELECT 
    status,
    COUNT(*) as quantity,
    SUM(amount) as total_amount
FROM pix_payment
GROUP BY status
ORDER BY status;

-- ============================================================
-- End of Seed
-- ============================================================
