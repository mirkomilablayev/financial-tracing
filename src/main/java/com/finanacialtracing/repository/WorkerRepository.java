package com.finanacialtracing.repository;

import com.finanacialtracing.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findAllByUserIdAndIsDeleted(Long userId, Boolean isDeleted);
    List<Worker> findAllByOrgIdAndIsDeleted(Long orgId, Boolean isDeleted);
    Boolean existsByIdAndIsDeleted(Long id, Boolean isDeleted);
    Boolean existsByUserIdAndOrgId(Long userId, Long orgId);
    Boolean existsByUserIdAndOrgIdAndIsDeleted(Long userId, Long orgId, Boolean isDeleted);
    Optional<Worker> findByIdAndIsDeleted(Long id, Boolean isDeleted);
    Optional<Worker> findByUserIdAndOrgIdAndIsDeleted(Long userId, Long orgId, Boolean isDeleted);
}
