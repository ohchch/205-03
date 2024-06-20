package com.example.billingservice.payments;

import org.springframework.stereotype.Component;

@Component
public class PaymentProcessorFactory {

    // Create instances of payment processors
    private final GooglePayProcessor gpayProcessor = new GooglePayProcessor();
    private final CreditCardProcessor cardProcessor = new CreditCardProcessor();

    // Method to get the appropriate payment processor based on the payment mode
    public PaymentProcessor getPaymentProcessor(String mode) {
        if ("gpay".equalsIgnoreCase(mode)) {
            return gpayProcessor;
        } else if ("credit_card".equalsIgnoreCase(mode)) {
            return cardProcessor;
        }
        return null;
    }
}

