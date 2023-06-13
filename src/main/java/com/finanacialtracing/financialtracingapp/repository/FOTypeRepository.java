package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FOType;
import com.finanacialtracing.financialtracingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FOTypeRepository extends JpaRepository<FOType, Long> {
    Optional<FOType> findByIdAndIsDeletedAndIsPersonal(Long id, Boolean isDeleted, Boolean isPersonal);

    List<FOType> findAllByUserIdAndIsDeletedAndIsPersonal(Long userId, Boolean isDeleted, Boolean isPersonal);
}
