package com.example.billingservice.repositories;

import com.example.billingservice.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Mark this interface as a Spring Data JPA repository
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // Custom query method to check if a store exists by name, phone number, or email
    boolean existsByNameOrPhoneNumberOrEmail(String name, String phoneNumber, String email);
}
