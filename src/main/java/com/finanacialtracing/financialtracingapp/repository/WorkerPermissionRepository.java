package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FinancialOperation;
import com.finanacialtracing.financialtracingapp.entity.WorkerPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerPermissionRepository extends JpaRepository<WorkerPermission, Long> {
    Optional<WorkerPermission> findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
