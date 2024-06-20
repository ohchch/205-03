package com.example.billingservice.payments;

public interface PaymentProcessor {

    // Method to process a payment
    // Returns true if the payment is successfully processed, otherwise false
    boolean process();
}
