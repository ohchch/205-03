package com.example.billingservice;

import org.springframework.stereotype.Service;

import com.example.billingservice.payments.PaymentProcessor;
import com.example.billingservice.payments.PaymentProcessorFactory;

@Service
public class BillingService {

    // Factory for creating payment processors based on the payment mode
    private final PaymentProcessorFactory paymentProcessorFactory;

    // Constructor that takes a PaymentProcessorFactory as a dependency
    // This enables the BillingService to create different payment processors
    public BillingService(PaymentProcessorFactory paymentProcessorFactory) {
        this.paymentProcessorFactory = paymentProcessorFactory;
    }

    // Method to complete payment using a specified payment mode
    // It retrieves the appropriate PaymentProcessor from the factory and processes the payment
    public boolean completePayment(String mode) {
        PaymentProcessor processor = paymentProcessorFactory.getPaymentProcessor(mode);
        return processor.process();
    }
}
