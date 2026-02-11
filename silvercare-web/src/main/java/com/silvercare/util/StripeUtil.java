package com.silvercare.util;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.util.HashMap;
import java.util.Map;

/**
 * Local utility for Stripe operations.
 * Replaces the need to call external REST APIs for payment logic.
 */
public class StripeUtil {

    static {
        // Initialize Stripe with secret key from Env Var or db.properties
        String secretKey = System.getenv("STRIPE_SECRET_KEY");
        if (secretKey == null) {
            secretKey = DBConnection.getProperty("stripe.secret.key");
        }

        if (secretKey != null) {
            Stripe.apiKey = secretKey;
        } else {
            System.err.println("StripeUtil: Stripe secret key not found!");
        }
    }

    /**
     * Create a Stripe PaymentIntent
     * Replaces /{id}/create-payment-intent API call
     */
    public static Map<String, Object> createPaymentIntent(double amount, String currency) throws Exception {
        // Amount in cents
        long amountInCents = Math.round(amount * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency != null ? currency : "sgd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, Object> result = new HashMap<>();
        result.put("clientSecret", intent.getClientSecret());
        result.put("paymentIntentId", intent.getId());
        return result;
    }
}
