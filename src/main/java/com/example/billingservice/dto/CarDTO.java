package com.example.billingservice.dto;

import org.springframework.web.multipart.MultipartFile;

public class CarDTO {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private String registration;
    private Double price;
    private MultipartFile image;
    private Long userId;
    private ActivateDTO carActivate;
    private String imagePath;
    private boolean active;
    private Double highestBiddingPrice;
    private String highestBidderEmail;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ActivateDTO getCarActivate() {
        return carActivate;
    }

    public void setCarActivate(ActivateDTO carActivate) {
        this.carActivate = carActivate;
    }

    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getHighestBiddingPrice() {
        return highestBiddingPrice;
    }

    public void setHighestBiddingPrice(Double highestBiddingPrice) {
        this.highestBiddingPrice = highestBiddingPrice;
    }

    public String getHighestBidderEmail() {
        return highestBidderEmail;
    }

    public void setHighestBidderEmail(String highestBidderEmail) {
        this.highestBidderEmail = highestBidderEmail;
    }

    
}
