package com.example.billingservice.payments;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    // Injecting PaymentProcessorFactory to handle different payment modes
    private final PaymentProcessorFactory paymentProcessorFactory;

    // Constructor-based dependency injection for PaymentProcessorFactory
    public PaymentController(PaymentProcessorFactory paymentProcessorFactory) {
        this.paymentProcessorFactory = paymentProcessorFactory;
    }

    // Handler for displaying the payment options page
    @GetMapping("/payment")
    public String showPaymentPage() {
        return "paymentOptions";
    }

    // Handler for completing a payment based on the specified mode
    @GetMapping("/completePayment")
    public String completePayment(@RequestParam("mode") String mode, Model model) {
        // Get the appropriate PaymentProcessor based on the mode
        PaymentProcessor processor = paymentProcessorFactory.getPaymentProcessor(mode);
        // Process the payment and determine if it was successful
        boolean success = processor != null && processor.process();

        // Add a message to the model based on the payment success or failure
        if (success) {
            model.addAttribute("message", "Payment with " + mode + " was successful!");
        } else {
            model.addAttribute("message", "Payment with " + mode + " failed.");
        }

        // Return the view name to be rendered (paymentResult.html)
        return "paymentResult";
    }
}
