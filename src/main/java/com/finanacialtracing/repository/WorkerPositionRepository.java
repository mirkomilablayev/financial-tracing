package com.finanacialtracing.repository;

import com.finanacialtracing.entity.WorkerPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerPositionRepository extends JpaRepository<WorkerPosition, Long> {
    Optional<WorkerPosition> findByIdAndIsDeleted(Long id, Boolean isDeleted);
    List<WorkerPosition> findAllByOrgIdAndIsDeleted(Long orgId, Boolean isDeleted);
}
