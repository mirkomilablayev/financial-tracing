package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FinancialOperation;
import com.finanacialtracing.financialtracingapp.entity.WorkerPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerPositionRepository extends JpaRepository<WorkerPosition, Long> {
}
