package com.example.billingservice.repositories;

import com.example.billingservice.entities.UserCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCarRepository extends JpaRepository<UserCar, Long> {
    void deleteByCar_Id(Long carId);
}
