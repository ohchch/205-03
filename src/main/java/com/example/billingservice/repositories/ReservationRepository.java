package com.example.billingservice.repositories;

import com.example.billingservice.entities.Car;
import com.example.billingservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCar(Car car);
    
    
}
