package com.example.billingservice.services;

import com.example.billingservice.repositories.UserRepository;
import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Car;
import com.example.billingservice.entities.Permissions;
import com.example.billingservice.entities.User;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.CarRepository;
import com.example.billingservice.repositories.PermissionRepository;

import jakarta.transaction.Transactional;

//import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final CarRepository carRepository;
    //private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, PermissionRepository permissionRepository, BCryptPasswordEncoder passwordEncoder, CarRepository carRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.carRepository = carRepository;
    }
    @Override
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);

        return user != null && passwordEncoder.matches(password, user.getPassword());
    }
    


    @Override
    public void saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Permissions userPermission = permissionRepository.findByPermissions("User");

        if (userPermission != null) {
            user.addPermission(userPermission);
        } else {
            throw new IllegalArgumentException("User permission not found");
        }
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String email) { // 这里的username实际是email
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        userRepository.deleteById(user.getId());
    }

    @Override
    public void addCarToUser(Long userId, Long carId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        user.addCar(car);
        userRepository.save(user);
    }
    

    @Override
    public void removeCarFromUser(Long userId, Long carId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        user.removeCar(car);
        userRepository.save(user);
    }

    @Override
    public List<CarDTO> getUserCars(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getCars().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setBrand(car.getBrand());
        carDTO.setModel(car.getModel());
        carDTO.setName(car.getName());
        carDTO.setPrice(car.getPrice());
        carDTO.setRegistration(car.getRegistration());
        carDTO.setImagePath(car.getImagePath());
        return carDTO;
    }
}
