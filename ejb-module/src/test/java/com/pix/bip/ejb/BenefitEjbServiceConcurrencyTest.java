package com.pix.bip.ejb;

import com.pix.bip.dto.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive concurrency and validation test suite for BenefitEjbServiceImpl.
 */
class BenefitEjbServiceConcurrencyTest {
    
    private BenefitEjbService service;
    private final String pixKey = "11111111-1111-1111-1111-111111111111";
    private final String receiverKey = "22222222-2222-2222-2222-222222222222";

    @BeforeEach
    void setUp() {
        service = new BenefitEjbServiceImpl();
        service.resetBalances();
    }

    @Test
    @DisplayName("Concurrent payments should expose race condition bug - EXPECTED TO FAIL")
    void testConcurrentPayments_ExposesBug() throws InterruptedException {
        BigDecimal paymentAmount = new BigDecimal("200.00");
        int numberOfThreads = 20;
        
        Long initialBalance = service.getBalance(pixKey);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CONCURRENCY BUG TEST - Negative Balance Attack");
        System.out.println("=".repeat(70));
        System.out.println("Initial Balance: R$ " + initialBalance / 100.0);
        System.out.println("Payment Amount: R$ " + paymentAmount);
        System.out.println("Number of Threads: " + numberOfThreads);
        System.out.println("Total Requested: R$ " + paymentAmount.multiply(BigDecimal.valueOf(numberOfThreads)));
        System.out.println("Expected Max Payments: " + (initialBalance / 20000) + " (should succeed)");
        System.out.println("Remainder Payments: " + (numberOfThreads - initialBalance / 20000) + " (should FAIL)");
        System.out.println("=".repeat(70));
        
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadNumber = i + 1;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    
                    PaymentRequest request = PaymentRequest.builder()
                        .pixKey(pixKey)
                        .receiverKey(receiverKey)
                        .amount(paymentAmount)
                        .description("Concurrent test #" + threadNumber)
                        .requestId(UUID.randomUUID().toString())
                        .build();
                    
                    service.processPayment(request);
                    successCount.incrementAndGet();
                    System.out.println("[SUCCESS] Thread " + threadNumber + " payment approved");
                    
                } catch (IllegalStateException e) {
                    failureCount.incrementAndGet();
                    System.out.println("[FAILED] Thread " + threadNumber + " payment rejected: " + e.getMessage());
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.err.println("[ERROR] Thread " + threadNumber + " unexpected error: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }
        
        System.out.println("\nReleasing all " + numberOfThreads + " threads simultaneously...\n");
        Thread.sleep(100);
        startLatch.countDown();
        
        boolean finished = doneLatch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();
        
        if (!finished) {
            System.err.println("\nWARNING: Some threads did not complete within 60 seconds!");
        }
        
        Long finalBalance = service.getBalance(pixKey);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST RESULTS:");
        System.out.println("=".repeat(70));
        System.out.println("Successful Payments: " + successCount.get());
        System.out.println("Failed Payments: " + failureCount.get());
        System.out.println("Final Balance: R$ " + finalBalance / 100.0);
        
        int expectedSuccessful = (int) (initialBalance / 20000);
        int expectedFailed = numberOfThreads - expectedSuccessful;
        Long expectedFinalBalance = initialBalance - (expectedSuccessful * 20000L);
        
        System.out.println("\nEXPECTED BEHAVIOR:");
        System.out.println("Should succeed: " + expectedSuccessful + " payments");
        System.out.println("Should fail: " + expectedFailed + " payments");
        System.out.println("Expected final balance: R$ " + expectedFinalBalance / 100.0);
        
        System.out.println("\nBUG DETECTION:");
        boolean bugDetected = false;
        
        if (finalBalance < 0) {
            System.err.println("[CRITICAL BUG] Balance became NEGATIVE!");
            System.err.println("   Final balance: R$ " + finalBalance / 100.0);
            System.err.println("   This should NEVER happen in production!");
            bugDetected = true;
        }
        
        if (successCount.get() > expectedSuccessful) {
            System.err.println("[RACE CONDITION BUG] More payments approved than balance allowed!");
            System.err.println("   Expected max: " + expectedSuccessful);
            System.err.println("   Actually approved: " + successCount.get());
            System.err.println("   Extra approvals: " + (successCount.get() - expectedSuccessful));
            bugDetected = true;
        }
        
        if (finalBalance != expectedFinalBalance) {
            System.err.println("[BALANCE INCONSISTENCY] Final balance differs from expected!");
            System.err.println("   Expected: R$ " + expectedFinalBalance / 100.0);
            System.err.println("   Actual: R$ " + finalBalance / 100.0);
            System.err.println("   Difference: R$ " + Math.abs(finalBalance - expectedFinalBalance) / 100.0);
            bugDetected = true;
        }
        
        if (!bugDetected) {
            System.out.println("WARNING: NO BUG DETECTED (threads may not have overlapped enough)");
            System.out.println("   Try running the test again or increasing thread count");
        }
        
        System.out.println("=".repeat(70) + "\n");
        
        assertTrue(finalBalance >= 0, 
            String.format("BUG EXPOSED: Balance became NEGATIVE! Final: R$ %.2f", finalBalance / 100.0));
        
        assertEquals(expectedSuccessful, successCount.get(),
            String.format("BUG EXPOSED: Race condition allowed %d extra payments!", 
                successCount.get() - expectedSuccessful));
    }

    @Test
    @DisplayName("Sequential payments should work correctly")
    void testSequentialPayments_WorksCorrectly() {
        BigDecimal paymentAmount = new BigDecimal("100.00");
        Long initialBalance = service.getBalance(pixKey);

        System.out.println("\n--- Sequential Payment Test ---");
        System.out.println("Initial balance: R$ " + initialBalance / 100.0);

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(paymentAmount)
            .description("Sequential test payment")
            .requestId(UUID.randomUUID().toString())
            .build();

        String paymentId = service.processPayment(request);
        assertNotNull(paymentId, "Payment ID should not be null");

        Long finalBalance = service.getBalance(pixKey);
        Long expectedBalance = initialBalance - 10000L;
        
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Final balance: R$ " + finalBalance / 100.0);
        System.out.println("Expected balance: R$ " + expectedBalance / 100.0);
        
        assertEquals(expectedBalance, finalBalance, 
            "Balance should decrease by exactly R$ 100.00");
        
        System.out.println("[PASS] Sequential payment test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with insufficient balance")
    void testPaymentFailsWithInsufficientBalance() {
        BigDecimal largeAmount = new BigDecimal("50000.00");
        Long currentBalance = service.getBalance(pixKey);

        System.out.println("\n--- Insufficient Balance Test ---");
        System.out.println("Current balance: R$ " + currentBalance / 100.0);
        System.out.println("Attempting to pay: R$ " + largeAmount);

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(largeAmount)
            .description("Test insufficient balance")
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalStateException exception = assertThrows(
            IllegalStateException.class, 
            () -> service.processPayment(request),
            "Should throw IllegalStateException for insufficient balance"
        );
        
        assertEquals("Insufficient balance", exception.getMessage(),
            "Exception message should indicate insufficient balance");

        Long balanceAfterFailure = service.getBalance(pixKey);
        assertEquals(currentBalance, balanceAfterFailure, 
            "Balance should remain unchanged after failed payment");
        
        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("Balance unchanged: R$ " + balanceAfterFailure / 100.0);
        System.out.println("[PASS] Insufficient balance test PASSED\n");
    }

    @Test
    @DisplayName("Payment to self should be rejected")
    void testCannotPayToSelf() {
        BigDecimal amount = new BigDecimal("100.00");

        System.out.println("\n--- Self-Payment Rejection Test ---");
        System.out.println("Attempting to pay R$ " + amount + " to self");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(pixKey)
            .amount(amount)
            .description("Self-payment test")
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for self-payment"
        );
        
        assertEquals("Sender and receiver cannot be the same", exception.getMessage(),
            "Exception message should indicate self-payment is not allowed");
        
        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Self-payment test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with null request")
    void testPaymentFailsWithNullRequest() {
        System.out.println("\n--- Null Request Test ---");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.processPayment(null),
            "Should throw IllegalArgumentException for null request"
        );

        assertEquals("Payment request cannot be null", exception.getMessage(),
            "Exception message should indicate null request");

        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Null request test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with null sender key")
    void testPaymentFailsWithNullSenderKey() {
        System.out.println("\n--- Null Sender Key Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(null)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("100.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for null sender key"
        );

        assertTrue(exception.getMessage().contains("Sender") || exception.getMessage().contains("PIX key"),
            "Exception message should mention sender or PIX key: " + exception.getMessage());

        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Null sender key test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with null receiver key")
    void testPaymentFailsWithNullReceiverKey() {
        System.out.println("\n--- Null Receiver Key Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(null)
            .amount(new BigDecimal("100.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for null receiver key"
        );

        assertTrue(exception.getMessage().contains("Receiver") || exception.getMessage().contains("receiver"),
            "Exception message should mention receiver: " + exception.getMessage());

        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Null receiver key test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with negative amount")
    void testPaymentFailsWithNegativeAmount() {
        System.out.println("\n--- Negative Amount Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("-50.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for negative amount"
        );

        assertTrue(exception.getMessage().contains("positive") || exception.getMessage().contains("Amount"),
            "Exception message should mention amount validation: " + exception.getMessage());

        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Negative amount test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with zero amount")
    void testPaymentFailsWithZeroAmount() {
        System.out.println("\n--- Zero Amount Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(BigDecimal.ZERO)
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for zero amount"
        );

        assertTrue(exception.getMessage().contains("positive") || exception.getMessage().contains("Amount"),
            "Exception message should mention amount validation: " + exception.getMessage());

        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Zero amount test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail with non-existent sender")
    void testPaymentFailsWithNonExistentSender() {
        System.out.println("\n--- Non-existent Sender Test ---");

        String nonExistentKey = "99999999-9999-9999-9999-999999999999";

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(nonExistentKey)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("100.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for non-existent sender"
        );

        assertTrue(exception.getMessage().contains("Sender") || exception.getMessage().contains("not found"),
            "Exception message should indicate sender was not found: " + exception.getMessage());

        System.out.println("Exception thrown: " + exception.getMessage());
        System.out.println("[PASS] Non-existent sender test PASSED\n");
    }

    @Test
    @DisplayName("Balance query should return correct value")
    void testBalanceQueryReturnsCorrectValue() {
        System.out.println("\n--- Balance Query Test ---");

        Long balance = service.getBalance(pixKey);
        
        assertNotNull(balance, "Balance should not be null");
        assertEquals(100000L, balance, "Initial balance should be R$ 1,000.00 (100000 cents)");
        
        System.out.println("Balance for " + pixKey + ": R$ " + balance / 100.0);
        System.out.println("[PASS] Balance query test PASSED\n");
    }

    @Test
    @DisplayName("Balance query should return zero for non-existent account")
    void testBalanceQueryForNonExistentAccount() {
        System.out.println("\n--- Non-existent Account Balance Test ---");

        String nonExistentKey = "00000000-0000-0000-0000-000000000000";
        Long balance = service.getBalance(nonExistentKey);
        
        assertNotNull(balance, "Balance should not be null");
        assertEquals(0L, balance, "Balance for non-existent account should be zero");
        
        System.out.println("Balance for non-existent account: R$ " + balance / 100.0);
        System.out.println("[PASS] Non-existent account balance test PASSED\n");
    }

    @Test
    @DisplayName("Multiple sequential payments should maintain balance consistency")
    void testMultipleSequentialPayments() {
        System.out.println("\n--- Multiple Sequential Payments Test ---");

        BigDecimal paymentAmount = new BigDecimal("50.00");
        int numberOfPayments = 10;
        Long initialBalance = service.getBalance(pixKey);

        System.out.println("Initial balance: R$ " + initialBalance / 100.0);
        System.out.println("Number of payments: " + numberOfPayments);

        for (int i = 0; i < numberOfPayments; i++) {
            PaymentRequest request = PaymentRequest.builder()
                .pixKey(pixKey)
                .receiverKey(receiverKey)
                .amount(paymentAmount)
                .description("Sequential payment #" + (i + 1))
                .requestId(UUID.randomUUID().toString())
                .build();

            service.processPayment(request);
        }

        Long finalBalance = service.getBalance(pixKey);
        Long expectedBalance = initialBalance - (5000L * numberOfPayments);

        System.out.println("Final balance: R$ " + finalBalance / 100.0);
        System.out.println("Expected balance: R$ " + expectedBalance / 100.0);

        assertEquals(expectedBalance, finalBalance,
            "Balance should decrease by exactly R$ 500.00 after 10 payments");

        System.out.println("[PASS] Multiple sequential payments test PASSED\n");
    }

    @Test
    @DisplayName("Payment with null amount should fail")
    void testPaymentFailsWithNullAmount() {
        System.out.println("\n--- Null Amount Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(null)
            .requestId(UUID.randomUUID().toString())
            .build();

        assertThrows(IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for null amount");

        System.out.println("[PASS] Null amount test PASSED\n");
    }

    @Test
    @DisplayName("Payment with empty sender key should fail")
    void testPaymentFailsWithEmptySenderKey() {
        System.out.println("\n--- Empty Sender Key Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey("")
            .receiverKey(receiverKey)
            .amount(new BigDecimal("100.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        assertThrows(IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for empty sender key");

        System.out.println("[PASS] Empty sender key test PASSED\n");
    }

    @Test
    @DisplayName("Payment with empty receiver key should fail")
    void testPaymentFailsWithEmptyReceiverKey() {
        System.out.println("\n--- Empty Receiver Key Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey("")
            .amount(new BigDecimal("100.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        assertThrows(IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for empty receiver key");

        System.out.println("[PASS] Empty receiver key test PASSED\n");
    }

    @Test
    @DisplayName("Payment with very small amount should work")
    void testPaymentWithVerySmallAmount() {
        System.out.println("\n--- Very Small Amount Test ---");

        BigDecimal smallAmount = new BigDecimal("0.01");
        Long initialBalance = service.getBalance(pixKey);

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(smallAmount)
            .requestId(UUID.randomUUID().toString())
            .build();

        String paymentId = service.processPayment(request);
        assertNotNull(paymentId, "Payment should succeed");

        Long finalBalance = service.getBalance(pixKey);
        assertEquals(initialBalance - 1L, finalBalance,
            "Balance should decrease by 1 cent");

        System.out.println("Payment successful with R$ 0.01");
        System.out.println("[PASS] Very small amount test PASSED\n");
    }

    @Test
    @DisplayName("Concurrent balance queries should return consistent results")
    void testConcurrentBalanceQueries() throws InterruptedException {
        System.out.println("\n--- Concurrent Balance Queries Test ---");

        int numberOfThreads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        List<Long> balances = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    balances.add(service.getBalance(pixKey));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        long firstBalance = balances.get(0);
        boolean allEqual = balances.stream().allMatch(b -> b.equals(firstBalance));

        assertTrue(allEqual, "All concurrent balance queries should return the same value");
        assertEquals(100000L, firstBalance, "Balance should be initial value");

        System.out.println("All " + balances.size() + " queries returned: R$ " + firstBalance / 100.0);
        System.out.println("[PASS] Concurrent balance queries test PASSED\n");
    }

    @Test
    @DisplayName("Reset balances should restore initial state")
    void testResetBalances() {
        System.out.println("\n--- Reset Balances Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("500.00"))
            .requestId(UUID.randomUUID().toString())
            .build();

        service.processPayment(request);
        Long balanceAfterPayment = service.getBalance(pixKey);

        System.out.println("Balance after payment: R$ " + balanceAfterPayment / 100.0);

        service.resetBalances();
        Long balanceAfterReset = service.getBalance(pixKey);

        System.out.println("Balance after reset: R$ " + balanceAfterReset / 100.0);

        assertEquals(100000L, balanceAfterReset,
            "Balance should be restored to initial value after reset");

        System.out.println("[PASS] Reset balances test PASSED\n");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.99", "1.00", "10.00", "100.00", "999.99"})
    @DisplayName("Parameterized test: Various payment amounts should work")
    void testVariousPaymentAmounts(String amountStr) {
        System.out.println("\n--- Testing payment amount: R$ " + amountStr + " ---");

        BigDecimal amount = new BigDecimal(amountStr);
        Long initialBalance = service.getBalance(pixKey);

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(amount)
            .requestId(UUID.randomUUID().toString())
            .build();

        String paymentId = service.processPayment(request);
        assertNotNull(paymentId, "Payment should succeed for amount: " + amountStr);

        Long finalBalance = service.getBalance(pixKey);
        long expectedDeduction = amount.multiply(new BigDecimal("100")).longValue();
        assertEquals(initialBalance - expectedDeduction, finalBalance,
            "Balance should decrease correctly for amount: " + amountStr);

        service.resetBalances(); // Reset for next iteration

        System.out.println("[PASS] Payment test PASSED for amount: R$ " + amountStr + "\n");
    }

    @RepeatedTest(3)
    @DisplayName("Repeated test: Race condition should be consistently reproducible")
    void testRaceConditionReproducibility() throws InterruptedException {
        BigDecimal paymentAmount = new BigDecimal("250.00");
        int numberOfThreads = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    PaymentRequest request = PaymentRequest.builder()
                        .pixKey(pixKey)
                        .receiverKey(receiverKey)
                        .amount(paymentAmount)
                        .requestId(UUID.randomUUID().toString())
                        .build();
                    service.processPayment(request);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        Long finalBalance = service.getBalance(pixKey);
        assertTrue(finalBalance >= 0, "Balance should never be negative in any iteration");
    }

    @Test
    @DisplayName("Stress test: Many small concurrent payments")
    @Timeout(30)
    void testStressTestManySmallPayments() throws InterruptedException {
        System.out.println("\n--- Stress Test: Many Small Payments ---");

        BigDecimal smallAmount = new BigDecimal("1.00");
        int numberOfThreads = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    PaymentRequest request = PaymentRequest.builder()
                        .pixKey(pixKey)
                        .receiverKey(receiverKey)
                        .amount(smallAmount)
                        .requestId(UUID.randomUUID().toString())
                        .build();
                    service.processPayment(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        Long finalBalance = service.getBalance(pixKey);

        System.out.println("Successful payments: " + successCount.get());
        System.out.println("Failed payments: " + failureCount.get());
        System.out.println("Final balance: R$ " + finalBalance / 100.0);

        assertTrue(finalBalance >= 0, "Balance should never be negative");
        assertEquals(successCount.get() + failureCount.get(), numberOfThreads,
            "All payments should be processed");

        System.out.println("[PASS] Stress test PASSED\n");
    }

    @Test
    @DisplayName("Payment should fail when exactly depleting balance")
    void testPaymentDepletesExactBalance() {
        System.out.println("\n--- Exact Balance Depletion Test ---");

        Long currentBalance = service.getBalance(pixKey);
        BigDecimal exactAmount = new BigDecimal(currentBalance / 100.0);

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(exactAmount)
            .requestId(UUID.randomUUID().toString())
            .build();

        String paymentId = service.processPayment(request);
        assertNotNull(paymentId, "Payment should succeed");

        Long finalBalance = service.getBalance(pixKey);
        assertEquals(0L, finalBalance, "Balance should be exactly zero");

        // Try another payment - should fail
        PaymentRequest failRequest = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("0.01"))
            .requestId(UUID.randomUUID().toString())
            .build();

        assertThrows(IllegalStateException.class,
            () -> service.processPayment(failRequest),
            "Payment should fail when balance is zero");

        System.out.println("[PASS] Exact balance depletion test PASSED\n");
    }

    @Test
    @DisplayName("Concurrent payments to multiple different receivers")
    void testConcurrentPaymentsToDifferentReceivers() throws InterruptedException {
        System.out.println("\n--- Concurrent Different Receivers Test ---");

        BigDecimal paymentAmount = new BigDecimal("10.00");
        int numberOfReceivers = 20;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfReceivers);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfReceivers);
        AtomicInteger successCount = new AtomicInteger(0);

        Long initialBalance = service.getBalance(pixKey);

        for (int i = 0; i < numberOfReceivers; i++) {
            final String receiver = "receiver-" + UUID.randomUUID();
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    PaymentRequest request = PaymentRequest.builder()
                        .pixKey(pixKey)
                        .receiverKey(receiver)
                        .amount(paymentAmount)
                        .requestId(UUID.randomUUID().toString())
                        .build();
                    service.processPayment(request);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(30, TimeUnit.SECONDS);
        executorService.shutdown();

        Long finalBalance = service.getBalance(pixKey);

        System.out.println("Initial balance: R$ " + initialBalance / 100.0);
        System.out.println("Successful payments: " + successCount.get());
        System.out.println("Final balance: R$ " + finalBalance / 100.0);

        assertTrue(finalBalance >= 0, "Balance should never be negative");
        assertTrue(successCount.get() <= numberOfReceivers,
            "Successful payments should not exceed attempted payments");

        System.out.println("[PASS] Concurrent different receivers test PASSED\n");
    }

    @Test
    @DisplayName("Payment with null requestId should fail")
    void testPaymentFailsWithNullRequestId() {
        System.out.println("\n--- Null RequestId Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("100.00"))
            .requestId(null)
            .build();

        assertThrows(IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for null requestId");

        System.out.println("[PASS] Null requestId test PASSED\n");
    }

    @Test
    @DisplayName("Payment with empty requestId should fail")
    void testPaymentFailsWithEmptyRequestId() {
        System.out.println("\n--- Empty RequestId Test ---");

        PaymentRequest request = PaymentRequest.builder()
            .pixKey(pixKey)
            .receiverKey(receiverKey)
            .amount(new BigDecimal("100.00"))
            .requestId("")
            .build();

        assertThrows(IllegalArgumentException.class,
            () -> service.processPayment(request),
            "Should throw IllegalArgumentException for empty requestId");

        System.out.println("[PASS] Empty requestId test PASSED\n");
    }
}