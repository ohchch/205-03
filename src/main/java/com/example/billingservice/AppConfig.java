package com.example.billingservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.billingservice.payments.CreditCardProcessor;
import com.example.billingservice.payments.GooglePayProcessor;
import com.example.billingservice.payments.PaymentProcessorFactory;

@Configuration
public class AppConfig {
    
    // Define a bean for GooglePayProcessor
    // This method will be called by Spring to create an instance of GooglePayProcessor
    @Bean
    public GooglePayProcessor googlePayProcessor() {
        return new GooglePayProcessor();
    }

    // Define a bean for CreditCardProcessor
    // This method will be called by Spring to create an instance of CreditCardProcessor
    @Bean
    public CreditCardProcessor creditCardProcessor() {
        return new CreditCardProcessor();
    }

    // Define a bean for PaymentProcessorFactory
    // This method will be called by Spring to create an instance of PaymentProcessorFactory
    @Bean
    public PaymentProcessorFactory paymentProcessorFactory() {
        return new PaymentProcessorFactory();
    }
}