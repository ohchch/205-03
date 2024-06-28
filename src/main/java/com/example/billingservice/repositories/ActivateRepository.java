package com.example.billingservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.billingservice.entities.Activate;

@Repository
public interface ActivateRepository extends JpaRepository<Activate, Long> {
    Optional<Activate> findByStatus(String status);
    Optional<Activate> findById(Integer id);
}


