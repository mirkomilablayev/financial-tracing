package com.finanacialtracing.repository;


import com.finanacialtracing.entity.UserRole;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends org.springframework.data.jpa.repository.JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);

}
