package com.silvercare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.silvercare.payment.StripePaymentProcessor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * REST Controller for Payment Processing
 * Handles Stripe PaymentIntent creation and verification
 */
@RestController
@RequestMapping("/payment")
public class PaymentRestController {

    @Autowired
    private StripePaymentProcessor paymentProcessor;

    /**
     * Create a PaymentIntent for Stripe checkout
     * POST /api/payment/create-intent
     * Body: { "amount": 100.50, "currency": "sgd", "description": "Booking #123" }
     */
    @PostMapping("/create-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            // Extract amount
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String currency = request.getOrDefault("currency", "sgd").toString();
            String description = request.getOrDefault("description", "SilverCare Service").toString();

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Amount must be greater than 0"));
            }

            // Create PaymentIntent via Stripe
            Map<String, Object> intentData = paymentProcessor.createPaymentIntent(amount, currency, description);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "clientSecret", intentData.get("clientSecret"),
                    "paymentIntentId", intentData.get("paymentIntentId")));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create payment intent: " + e.getMessage()));
        }
    }

    /**
     * Verify a PaymentIntent status
     * GET /api/payment/verify/{paymentIntentId}
     */
    @GetMapping("/verify/{paymentIntentId}")
    public ResponseEntity<?> verifyPayment(@PathVariable String paymentIntentId) {
        try {
            Map<String, Object> paymentData = paymentProcessor.verifyPaymentIntent(paymentIntentId);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "paymentStatus", paymentData.get("status"),
                    "amount", paymentData.get("amount"),
                    "currency", paymentData.get("currency")));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to verify payment: " + e.getMessage()));
        }
    }

    /**
     * Process a simple payment (for testing/backwards compatibility)
     * POST /api/payment/process
     * Body: { "amount": 100.50, "cardDetails": "4242424242424242" }
     */
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> request) {
        try {
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String cardDetails = request.getOrDefault("cardDetails", "").toString();

            boolean success = paymentProcessor.processPayment(amount, cardDetails);

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Payment processed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                        .body(Map.of("error", "Payment declined"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment processing failed: " + e.getMessage()));
        }
    }
}
