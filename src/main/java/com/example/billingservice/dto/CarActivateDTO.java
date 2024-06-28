package com.example.billingservice.dto;

public class CarActivateDTO {
    private Long carId;
    private Integer activateId;

    // Constructors, getters, and setters
    public CarActivateDTO() {
    }

    public CarActivateDTO(Long carId, Integer activateId) {
        this.carId = carId;
        this.activateId = activateId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Integer getActivateId() {
        return activateId;
    }

    public void setActivateId(Integer activateId) {
        this.activateId = activateId;
    }
}
