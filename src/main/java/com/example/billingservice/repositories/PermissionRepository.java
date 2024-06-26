package com.example.billingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.billingservice.entities.Permissions;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Long> {
    Permissions findByPermissions(String permissions);
}
