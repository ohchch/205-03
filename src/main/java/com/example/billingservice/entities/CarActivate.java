package com.example.billingservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "car_activate")
public class CarActivate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activate_id")
    private Activate activate;

    // Constructors, getters, and setters

    public CarActivate() {
    }

    public CarActivate(Car car, Activate activate) {
        this.car = car;
        this.activate = activate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Activate getActivate() {
        return activate;
    }

    public void setActivate(Activate activate) {
        this.activate = activate;
    }
}
