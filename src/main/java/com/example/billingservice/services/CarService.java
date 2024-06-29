package com.example.billingservice.services;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Car;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarService {
    List<CarDTO> getAllCars();

    List<CarDTO> getActivatedCars();

    List<CarDTO> getDeactivatedCars();

    List<CarDTO> searchCars(String keyword);

    CarDTO getCarById(Long id) throws ResourceNotFoundException;

    CarDTO addCar(CarDTO carDTO, MultipartFile image, Long userId) throws IOException;

    CarDTO updateCar(CarDTO carDTO, MultipartFile image) throws ResourceNotFoundException, IOException;

    void deleteCarById(Long id) throws ResourceNotFoundException;

    void updateActivateStatus(Car car, Integer activateId) ;
    
    void toggleActivate(Long carId) throws ResourceNotFoundException;
}
