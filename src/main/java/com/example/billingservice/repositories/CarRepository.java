package com.example.billingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.billingservice.entities.Car;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.carActivate ca LEFT JOIN FETCH ca.activate WHERE c.id = :id")
    Optional<Car> findByIdWithActivate(@Param("id") Long id);
}