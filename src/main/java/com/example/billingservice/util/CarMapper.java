package com.example.billingservice.util;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Car;

public class CarMapper {

    public static CarDTO entityToDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setName(car.getName());
        carDTO.setBrand(car.getBrand());
        carDTO.setModel(car.getModel());
        carDTO.setRegistration(car.getRegistration());
        carDTO.setPrice(car.getPrice());
        return carDTO;
    }

    public static Car dtoToEntity(CarDTO carDTO) {
        Car car = new Car();
        car.setId(carDTO.getId());
        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setRegistration(carDTO.getRegistration());
        car.setPrice(carDTO.getPrice());
        return car;
    }
}
