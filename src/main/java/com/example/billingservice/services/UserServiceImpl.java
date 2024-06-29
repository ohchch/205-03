package com.example.billingservice.services;

import com.example.billingservice.dto.ActivateDTO;
import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Activate;
import com.example.billingservice.entities.Car;
import com.example.billingservice.entities.Permissions;
import com.example.billingservice.entities.User;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.CarRepository;
import com.example.billingservice.repositories.PermissionRepository;
import com.example.billingservice.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final CarRepository carRepository;


    public UserServiceImpl(UserRepository userRepository, PermissionRepository permissionRepository,
                           BCryptPasswordEncoder passwordEncoder, CarRepository carRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.carRepository = carRepository;
    }

    @Override
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new BadCredentialsException("UserDetailsService returned null for: " + email);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password for: " + email);
        }
        return true;
    }

    @Override
    public void saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
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
    public User findByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    @Override
    public User findByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public void changeEmail(Long userId, String newEmail) {
        User user = getUserById(userId);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
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
        return user.getCars().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setName(car.getName());
        carDTO.setBrand(car.getBrand());
        carDTO.setModel(car.getModel());
        carDTO.setRegistration(car.getRegistration());
        carDTO.setPrice(car.getPrice());
        carDTO.setImagePath(car.getImagePath());

        if (car.getCarActivate() != null && car.getCarActivate().getActivate() != null) {
            Activate activate = car.getCarActivate().getActivate();
            ActivateDTO activateDTO = new ActivateDTO();
            activateDTO.setId(activate.getId());
            activateDTO.setStatus(activate.getStatus());
            carDTO.setCarActivate(activateDTO);
            carDTO.setActive("Activated".equals(activate.getStatus()));
        }

        return carDTO;
    }
}
