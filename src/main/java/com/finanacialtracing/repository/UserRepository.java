package com.finanacialtracing.repository;

import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    Optional<User> findByUsernameAndIsDeleted(String username, Boolean isDeleted);


    Boolean existsByUsernameAndIsDeleted(String username, Boolean isDeleted);

    Boolean existsByIdAndIsDeleted(Long id, Boolean isDeleted);

//    List<User> findAllByRolesAndIsDeleted(Set<UserRole> roles, Boolean isDeleted);
}
