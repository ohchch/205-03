package com.example.billingservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bidding")
public class Bidding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "bidding_price", nullable = false)
    private Double biddingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors, getters, and setters
    public Bidding() {
    }

    public Bidding(Car car, Double biddingPrice, User user) {
        this.car = car;
        this.biddingPrice = biddingPrice;
        this.user = user;
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

    public Double getBiddingPrice() {
        return biddingPrice;
    }

    public void setBiddingPrice(Double biddingPrice) {
        this.biddingPrice = biddingPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
