package com.example.billingservice.repositories;

import com.example.billingservice.entities.Bidding;
import com.example.billingservice.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiddingRepository extends JpaRepository<Bidding, Long> {
    Bidding findTopByCarOrderByBiddingPriceDesc(Car car);
    void deleteByCar(Car car);
}
