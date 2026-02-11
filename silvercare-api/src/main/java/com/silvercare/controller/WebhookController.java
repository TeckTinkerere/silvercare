package com.silvercare.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import java.util.Map;

/**
 * Webhook Controller for Stripe Events
 * Handles real-time notifications from Stripe about payment status changes
 */
@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    @Value("${stripe.webhook.secret:whsec_placeholder}")
    private String webhookSecret;

    // Note: bookingDAO would be used to update booking statuses when
    // payment_intent_id column is added
    // Currently kept for future implementation

    /**
     * Handle Stripe webhook events
     * POST /api/webhooks/stripe
     * 
     * Verifies webhook signature and processes payment events
     */
    @PostMapping("/stripe")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        // Verify webhook signature for security
        if (webhookSecret == null || webhookSecret.contains("placeholder")) {
            System.out.println("⚠️  Webhook secret not configured. Skipping signature verification (DEV MODE)");
        } else {
            try {
                Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
                return processEvent(event);
            } catch (SignatureVerificationException e) {
                System.err.println("❌ Invalid webhook signature: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid signature"));
            }
        }

        // In dev mode without signature verification, parse event manually
        try {
            Event event = Event.GSON.fromJson(payload, Event.class);
            return processEvent(event);
        } catch (Exception e) {
            System.err.println("❌ Error parsing webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid payload"));
        }
    }

    /**
     * Process different Stripe event types
     */
    private ResponseEntity<?> processEvent(Event event) {
        System.out.println("📨 Received Stripe Event: " + event.getType());

        switch (event.getType()) {
            case "payment_intent.succeeded":
                return handlePaymentSucceeded(event);

            case "payment_intent.payment_failed":
                return handlePaymentFailed(event);

            case "payment_intent.canceled":
                return handlePaymentCanceled(event);

            case "charge.refunded":
                return handleChargeRefunded(event);

            default:
                System.out.println("ℹ️  Unhandled event type: " + event.getType());
                return ResponseEntity.ok(Map.of("received", true));
        }
    }

    /**
     * Handle successful payment
     * Update booking status to "Paid" or "Confirmed"
     */
    private ResponseEntity<?> handlePaymentSucceeded(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (paymentIntent == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid payment intent data"));
            }

            String paymentIntentId = paymentIntent.getId();
            Long amountReceived = paymentIntent.getAmountReceived();

            System.out.println("✅ Payment Succeeded: " + paymentIntentId);
            System.out.println(
                    "   Amount: " + (amountReceived / 100.0) + " " + paymentIntent.getCurrency().toUpperCase());

            // Future: Update booking status in database
            // Requires adding payment_intent_id column to booking table
            // bookingDAO.updateBookingStatusByPaymentIntent(paymentIntentId, "Paid");

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Payment processed",
                    "paymentIntentId", paymentIntentId));

        } catch (Exception e) {
            System.err.println("❌ Error handling payment success: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Processing failed"));
        }
    }

    /**
     * Handle failed payment
     * Update booking status to "Payment Failed"
     */
    private ResponseEntity<?> handlePaymentFailed(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (paymentIntent == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid payment intent data"));
            }

            String paymentIntentId = paymentIntent.getId();
            String failureMessage = paymentIntent.getLastPaymentError() != null
                    ? paymentIntent.getLastPaymentError().getMessage()
                    : "Unknown error";

            System.out.println("❌ Payment Failed: " + paymentIntentId);
            System.out.println("   Reason: " + failureMessage);

            // Future: Update booking status
            // bookingDAO.updateBookingStatusByPaymentIntent(paymentIntentId, "Payment
            // Failed");

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Failure recorded",
                    "paymentIntentId", paymentIntentId));

        } catch (Exception e) {
            System.err.println("❌ Error handling payment failure: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Processing failed"));
        }
    }

    /**
     * Handle canceled payment
     */
    private ResponseEntity<?> handlePaymentCanceled(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (paymentIntent == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid payment intent data"));
            }

            String paymentIntentId = paymentIntent.getId();

            System.out.println("🚫 Payment Canceled: " + paymentIntentId);

            // Future: Update booking status
            // bookingDAO.updateBookingStatusByPaymentIntent(paymentIntentId, "Canceled");

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cancellation recorded",
                    "paymentIntentId", paymentIntentId));

        } catch (Exception e) {
            System.err.println("❌ Error handling payment cancellation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Processing failed"));
        }
    }

    /**
     * Handle charge refund
     */
    private ResponseEntity<?> handleChargeRefunded(Event event) {
        try {
            System.out.println("💸 Charge Refunded");

            // TODO: Update booking status to "Refunded"
            // Extract charge ID and update related booking

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Refund recorded"));

        } catch (Exception e) {
            System.err.println("❌ Error handling refund: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Processing failed"));
        }
    }
}
