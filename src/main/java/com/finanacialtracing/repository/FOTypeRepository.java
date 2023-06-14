package com.finanacialtracing.repository;

import com.finanacialtracing.entity.FOType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FOTypeRepository extends JpaRepository<FOType, Long> {
    Optional<FOType> findByIdAndIsDeletedAndIsPersonal(Long id, Boolean isDeleted, Boolean isPersonal);

    List<FOType> findAllByUserIdAndIsDeletedAndIsPersonal(Long userId, Boolean isDeleted, Boolean isPersonal);
    List<FOType> findAllByOrgIdAndIsDeletedAndIsPersonal(Long orgId, Boolean isDeleted, Boolean isPersonal);
}
