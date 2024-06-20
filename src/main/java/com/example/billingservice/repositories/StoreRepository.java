package com.example.billingservice.repositories;

import com.example.billingservice.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByNameOrPhoneNumberOrEmail(String name, String phoneNumber, String email);
}
