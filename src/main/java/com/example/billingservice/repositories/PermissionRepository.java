package com.example.billingservice.repositories;

import com.example.billingservice.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Long> {
    Permissions findByPermissions(String permissions);
}
