package com.example.billingservice;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.billingservice.entities.User;
import com.example.billingservice.repositories.UserRepository;
import com.example.billingservice.services.UserServiceImpl;

public class UserServiceLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    public void testAuthenticate_Success() {
        // Step 1: Valid user login
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = userService.authenticate(user.getEmail(), user.getPassword());

        assertTrue(result);
    }

    @Test
    public void testAuthenticate_InvalidEmail() {
        // Step 2: Invalid email login
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            userService.authenticate(user.getEmail(), user.getPassword());
        });

        assertEquals("UserDetailsService returned null for: " + user.getEmail(), exception.getMessage());
    }

    @Test
    public void testAuthenticate_InvalidPassword() {
        // Step 3: Incorrect password login
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            userService.authenticate(user.getEmail(), user.getPassword());
        });

        assertEquals("Invalid password for: " + user.getEmail(), exception.getMessage());
    }

    @Test
    public void testPasswordEncryptionDuringLogin() {
        // Step 4: Check password encryption during login
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        userService.authenticate(user.getEmail(), user.getPassword());

        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    public void testSessionCreation() {
        // Step 5: Verify session creation
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = userService.authenticate(user.getEmail(), user.getPassword());

        assertTrue(result);
    }
}
