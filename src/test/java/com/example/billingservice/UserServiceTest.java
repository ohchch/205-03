package com.example.billingservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.billingservice.model.User;
import com.example.billingservice.model.Permissions;
import com.example.billingservice.repositories.UserRepository;
import com.example.billingservice.repositories.PermissionRepository;
import com.example.billingservice.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        permissionRepository = mock(PermissionRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, permissionRepository, passwordEncoder);
    }

    /*@Test
    public void testUserAuthenticationSuccess() {
        String email = "user@example.com";
        String password = "password";
        User user = new User(email, passwordEncoder.encode(password));

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        assertTrue(userService.authenticate(email, password));
    }*/

    @Test
    public void testUserAuthenticationFailure() {
        String email = "user@example.com";
        String password = "wrong_password";

        when(userRepository.findByEmail(email)).thenReturn(null);

        assertFalse(userService.authenticate(email, password));
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");

        Permissions userPermission = new Permissions();
        userPermission.setPermissions("User");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
        when(permissionRepository.findByPermissions("User")).thenReturn(userPermission);

        userService.saveUser(user);

        assertEquals("encoded_password", user.getPassword());
        assertTrue(user.getPermissions().contains(userPermission));
        verify(userRepository).save(user);
    }

    @Test
    public void testSaveUserPermissionNotFound() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
        when(permissionRepository.findByPermissions("User")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("User permission not found", exception.getMessage());
    }

    @Test
    public void testDeleteUserByEmail() {
        String email = "user@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        userService.deleteUserByEmail(email);

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public void testDeleteUserByEmailNotFound() {
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserByEmail(email);
        });

        assertEquals("User not found with email: " + email, exception.getMessage());
    }
}
