package com.finanacialtracing.financialtracingapp.repository;


import com.finanacialtracing.financialtracingapp.entity.UserRole;

import java.util.Optional;

public interface RoleRepository extends org.springframework.data.jpa.repository.JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);
}
