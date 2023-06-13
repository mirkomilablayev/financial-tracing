package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FinancialOperation;
import com.finanacialtracing.financialtracingapp.entity.Organization;
import com.finanacialtracing.financialtracingapp.entity.User;
import com.finanacialtracing.financialtracingapp.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUserAndOrganizationAndIsDeleted(User user, Organization organization, Boolean isDeleted);
}
