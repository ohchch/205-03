package com.example.billingservice.services;

import com.example.billingservice.model.User;

public interface UserService {
    boolean authenticate(String email, String password);
    void saveUser(User user);
    User findByEmail(String email);
    void deleteUserByEmail(String email);
}
