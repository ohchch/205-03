package com.example.billingservice.config;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.billingservice.services.UserService;
import com.example.billingservice.exceptions.ResourceNotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    private final UserService userService;

    public CustomAuthenticationFailureHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorParameter = "";
        String email = request.getParameter("email");

        try {
            userService.findByEmail(email);
        } catch (ResourceNotFoundException e) {
            errorParameter = "userNotFound";
        }

        if (errorParameter.isEmpty()) {
            if (exception instanceof BadCredentialsException) {
                errorParameter = "invalidPassword";
            } else {
                errorParameter = "unknown";
            }
        }

        // Log the redirection and error parameter
        logger.info("Redirecting to /login with error parameter: {}", errorParameter);

        // Perform the redirect
        response.sendRedirect(request.getContextPath() + "/login?error=" + errorParameter);
    }
}

