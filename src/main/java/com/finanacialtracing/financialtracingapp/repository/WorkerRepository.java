package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FinancialOperation;
import com.finanacialtracing.financialtracingapp.entity.Organization;
import com.finanacialtracing.financialtracingapp.entity.User;
import com.finanacialtracing.financialtracingapp.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findAllByUserIdAndIsDeleted(Long userId, Boolean isDeleted);
    List<Worker> findAllByOrgIdAndIsDeleted(Long orgId, Boolean isDeleted);
    Boolean existsByIdAndIsDeleted(Long id, Boolean isDeleted);
    Boolean existsByUserIdAndOrgId(Long userId, Long orgId);
}
