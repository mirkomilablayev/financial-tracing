package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FinancialOperation;
import com.finanacialtracing.financialtracingapp.entity.WorkerPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerPermissionRepository extends JpaRepository<WorkerPermission, Long> {
}
