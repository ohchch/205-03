package com.example.billingservice.services;

import java.util.List;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.User;
import com.example.billingservice.exceptions.ResourceNotFoundException;

public interface UserService {
    boolean authenticate(String email, String password);
    void saveUser(User user);
    User findByEmail(String email) throws ResourceNotFoundException;
    User findByUsername(String email) throws ResourceNotFoundException;
    User getUserById(Long userId) throws ResourceNotFoundException;
    void changeEmail(Long userId, String newEmail);
    void changePassword(Long userId, String newPassword);
    void deleteUserByEmail(String email);
    void addCarToUser(Long userId, Long carId) throws ResourceNotFoundException;
    void removeCarFromUser(Long userId, Long carId) throws ResourceNotFoundException;
    List<CarDTO> getUserCars(Long userId) throws ResourceNotFoundException;
}
