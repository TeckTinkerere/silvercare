package com.silvercare.payment;

import java.math.BigDecimal;
import java.util.Random;

public class StripeMockProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(BigDecimal amount, String cardDetails) {
        System.out.println("Connecting to Stripe Payment Gateway...");
        
        try {
            // Simulate network latency
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate Validation
        if (cardDetails == null || cardDetails.replace(" ", "").length() != 16) {
            System.out.println("Payment Declined: Invalid Card Number.");
            return false;
        }

        System.out.println("Processing charge of $" + amount);
        
        // Randomly fail 10% of transactions to simulate real-world issues
        if (new Random().nextDouble() < 0.1) {
            System.out.println("Payment Failed: Insufficient Funds or Network Error.");
            return false;
        }

        System.out.println("Payment Approved! Transaction ID: " + java.util.UUID.randomUUID());
        return true;
    }
}
