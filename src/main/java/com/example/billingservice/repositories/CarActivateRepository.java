package com.example.billingservice.repositories;

import com.example.billingservice.entities.CarActivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarActivateRepository extends JpaRepository<CarActivate, Long> {

    CarActivate findByCarId(Long carId);

    @Transactional
    void deleteByCarId(Long carId);

}
