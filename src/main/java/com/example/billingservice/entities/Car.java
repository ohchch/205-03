package com.example.billingservice.entities;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "registration")
    private String registration;
    
    @Column(name = "price")
    private Double price;
    
    @Column(name = "image_path")
    private String imagePath;

    @Transient
    private MultipartFile imageFile;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private CarActivate carActivate;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserCar> userCars = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bidding> biddings;

    @Transient
    private Double highestBiddingPrice;

    public Car() {
    }

    public Car(String name, String brand, String model, String registration, Double price, String imagePath) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.registration = registration;
        this.price = price;
        this.imagePath = imagePath;
    }

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public CarActivate getCarActivate() {
        return carActivate;
    }

    public void setCarActivate(CarActivate carActivate) {
        this.carActivate = carActivate;
    }

    public Set<UserCar> getUserCars() {
        return userCars;
    }

    public void setUserCars(Set<UserCar> userCars) {
        this.userCars = userCars;
    }

    public Double getHighestBiddingPrice() {
        return biddings.stream()
                .map(Bidding::getBiddingPrice)
                .max(Double::compareTo)
                .orElse(0.00);
    }
}
