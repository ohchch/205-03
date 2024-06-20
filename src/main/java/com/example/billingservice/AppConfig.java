package com.example.billingservice;

// Import necessary Spring framework classes
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Import custom payment processor classes
import com.example.billingservice.payments.CreditCardProcessor;
import com.example.billingservice.payments.GooglePayProcessor;
import com.example.billingservice.payments.PaymentProcessorFactory;

// Mark this class as a configuration class for Spring
@Configuration
public class AppConfig {
    
    // Define a bean for GooglePayProcessor
    // This method will be called by Spring to create and manage an instance of GooglePayProcessor
    @Bean
    public GooglePayProcessor googlePayProcessor() {
        return new GooglePayProcessor();
    }

    // Define a bean for CreditCardProcessor
    // This method will be called by Spring to create and manage an instance of CreditCardProcessor
    @Bean
    public CreditCardProcessor creditCardProcessor() {
        return new CreditCardProcessor();
    }

    // Define a bean for PaymentProcessorFactory
    // This method will be called by Spring to create and manage an instance of PaymentProcessorFactory
    @Bean
    public PaymentProcessorFactory paymentProcessorFactory() {
        return new PaymentProcessorFactory();
    }
}
