package com.silvercare.payment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

/**
 * Real Stripe Payment Integration for SilverCare
 * Handles secure payment processing for elderly care services
 */
@Service
public class StripePaymentProcessor implements PaymentProcessor {

    @Value("${stripe.secret.key:sk_test_placeholder}")
    private String stripeSecretKey;

    @Value("${stripe.mock.enabled:true}")
    private boolean useMock;

    // Constructor for manual instantiation (when not using Spring DI)
    public StripePaymentProcessor() {
        // Will be initialized via init() or @PostConstruct
    }

    @PostConstruct
    public void init() {
        // Try to get from environment if not injected
        if (stripeSecretKey == null || stripeSecretKey.contains("placeholder")) {
            stripeSecretKey = System.getenv("STRIPE_SECRET_KEY");
        }

        String mockEnabledStr = System.getenv("STRIPE_MOCK_ENABLED");
        if (mockEnabledStr != null) {
            useMock = Boolean.parseBoolean(mockEnabledStr);
        }

        if (!useMock && stripeSecretKey != null && !stripeSecretKey.contains("placeholder")) {
            Stripe.apiKey = stripeSecretKey;
            System.out.println("✅ Stripe initialized with API key");
        } else {
            System.out.println("⚠️  Stripe running in MOCK mode (no real charges)");
        }
    }

    @Override
    public boolean processPayment(BigDecimal amount, String cardDetails) {
        // Initialize if not already done
        if (Stripe.apiKey == null && !useMock) {
            init();
        }

        if (useMock || stripeSecretKey == null || stripeSecretKey.contains("placeholder")) {
            System.out.println("💳 [MOCK MODE] Processing payment of SGD " + amount);
            System.out.println("   Card: "
                    + (cardDetails != null ? cardDetails.substring(0, Math.min(4, cardDetails.length())) + "..."
                            : "N/A"));
            System.out.println("   ✅ Payment approved (mock)");
            return true;
        }

        try {
            long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("sgd") // Singapore Dollar for elderly care services
                    .setDescription("SilverCare Service Payment")
                    .putMetadata("service_type", "elderly_care")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Log successful payment for audit trail
            System.out.println("✅ Payment Intent Created: " + paymentIntent.getId());
            System.out.println("   Amount: SGD " + amount);
            System.out.println("   Status: " + paymentIntent.getStatus());

            return "succeeded".equals(paymentIntent.getStatus()) ||
                    "requires_confirmation".equals(paymentIntent.getStatus()) ||
                    "requires_payment_method".equals(paymentIntent.getStatus());

        } catch (StripeException e) {
            System.err.println("❌ Stripe Payment Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Process recurring payment for subscription-based care services
     */
    public boolean processRecurringPayment(BigDecimal amount, String customerId, String paymentMethodId) {
        if (useMock) {
            System.out.println("[MOCK] Processing recurring payment of SGD " + amount + " for customer " + customerId);
            return true;
        }

        try {
            long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("sgd")
                    .setCustomer(customerId)
                    .setPaymentMethod(paymentMethodId)
                    .setDescription("SilverCare Recurring Service Payment")
                    .setConfirm(true)
                    .putMetadata("payment_type", "recurring")
                    .putMetadata("service_category", "elderly_care_subscription")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(
                                            PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return "succeeded".equals(paymentIntent.getStatus());

        } catch (StripeException e) {
            System.err.println("Recurring Payment Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create a PaymentIntent for Stripe checkout
     * This allows frontend to handle payment confirmation securely
     */
    public Map<String, Object> createPaymentIntent(BigDecimal amount, String currency, String description) {
        Map<String, Object> result = new HashMap<>();

        // Initialize if not already done
        if (Stripe.apiKey == null && !useMock) {
            init();
        }

        if (useMock || stripeSecretKey == null || stripeSecretKey.contains("placeholder")) {
            System.out.println("💳 [MOCK MODE] Creating PaymentIntent for " + currency.toUpperCase() + " " + amount);
            result.put("clientSecret", "pi_mock_secret_" + System.currentTimeMillis());
            result.put("paymentIntentId", "pi_mock_" + System.currentTimeMillis());
            result.put("status", "requires_payment_method");
            return result;
        }

        try {
            long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency.toLowerCase())
                    .setDescription(description)
                    .putMetadata("service_type", "elderly_care")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            result.put("clientSecret", paymentIntent.getClientSecret());
            result.put("paymentIntentId", paymentIntent.getId());
            result.put("status", paymentIntent.getStatus());

            System.out.println("✅ PaymentIntent Created: " + paymentIntent.getId());
            return result;

        } catch (StripeException e) {
            System.err.println("❌ Stripe PaymentIntent Error: " + e.getMessage());
            throw new RuntimeException("Failed to create payment intent", e);
        }
    }

    /**
     * Verify the status of a PaymentIntent
     */
    public Map<String, Object> verifyPaymentIntent(String paymentIntentId) {
        Map<String, Object> result = new HashMap<>();

        if (useMock || paymentIntentId.startsWith("pi_mock_")) {
            System.out.println("💳 [MOCK MODE] Verifying PaymentIntent: " + paymentIntentId);
            result.put("status", "succeeded");
            result.put("amount", 10000L);
            result.put("currency", "sgd");
            result.put("paymentIntentId", paymentIntentId);
            return result;
        }

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            result.put("status", paymentIntent.getStatus());
            result.put("amount", paymentIntent.getAmount());
            result.put("currency", paymentIntent.getCurrency());
            result.put("paymentIntentId", paymentIntent.getId());

            System.out.println(
                    "✅ PaymentIntent Verified: " + paymentIntent.getId() + " - Status: " + paymentIntent.getStatus());
            return result;

        } catch (StripeException e) {
            System.err.println("❌ Stripe Verify Error: " + e.getMessage());
            throw new RuntimeException("Failed to verify payment intent", e);
        }
    }
}
