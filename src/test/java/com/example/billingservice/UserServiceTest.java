package com.example.billingservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.billingservice.entities.Permissions;
import com.example.billingservice.entities.User;
import com.example.billingservice.repositories.PermissionRepository;
import com.example.billingservice.repositories.UserRepository;
import com.example.billingservice.services.UserServiceImpl;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PermissionRepository permissionRepository;

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
    public void testSaveUser_Success() {
        // Step 1: Run the test script
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        Permissions userPermission = new Permissions();
        userPermission.setPermissions("User");
        when(permissionRepository.findByPermissions("User")).thenReturn(userPermission);

        userService.saveUser(user);

        assertNotNull(user.getPermissions());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveUser_EmailAlreadyInUse() {
        // Step 2: Check for email duplication
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testSaveUser_PermissionNotFound() {
        // Step 3: Validate password encryption and Step 4: Verify permission assignment
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(permissionRepository.findByPermissions("User")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("User permission not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
