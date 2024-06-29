package com.example.billingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.billingservice.entities.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.carActivate ca LEFT JOIN FETCH ca.activate WHERE c.id = :id")
    Optional<Car> findByIdWithActivate(@Param("id") Long id);

    List<Car> findByCarActivate_Activate_Status(String status);

    @Query("SELECT c FROM Car c WHERE lower(c.name) LIKE lower(concat('%', :keyword, '%')) " +
           "OR lower(c.brand) LIKE lower(concat('%', :keyword, '%')) " +
           "OR lower(c.model) LIKE lower(concat('%', :keyword, '%')) " +
           "OR lower(c.registration) LIKE lower(concat('%', :keyword, '%'))")
    List<Car> search(@Param("keyword") String keyword);
}