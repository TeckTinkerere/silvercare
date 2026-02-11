package com.silvercare.payment;

import java.math.BigDecimal;

public interface PaymentProcessor {
    /**
     * Process a payment.
     * @param amount The amount to charge.
     * @param cardDetails Dummy card details map or string.
     * @return true if successful, false otherwise.
     */
    boolean processPayment(BigDecimal amount, String cardDetails);
}
