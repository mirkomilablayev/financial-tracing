package com.finanacialtracing.repository;

import com.finanacialtracing.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FOTypeRepository extends JpaRepository<TransactionType, Long> {
    Optional<TransactionType> findByIdAndIsDeletedAndIsPersonal(Long id, Boolean isDeleted, Boolean isPersonal);

    List<TransactionType> findAllByUserIdAndIsDeletedAndIsPersonal(Long userId, Boolean isDeleted, Boolean isPersonal);
    List<TransactionType> findAllByOrgIdAndIsDeletedAndIsPersonal(Long orgId, Boolean isDeleted, Boolean isPersonal);
}
