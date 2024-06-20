package com.example.billingservice.payments;

// Implementation of PaymentProcessor for Google Pay payments
public class GooglePayProcessor implements PaymentProcessor {

    // Process a Google Pay payment
    // Returns true if the payment is successfully processed
    @Override
    public boolean process() {
        // Simulate payment processing
        return true;
    }
}
