package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.financialoperation.organization.OrganizationFinancialOperationCriteria;
import com.finanacialtracing.dto.worker.UpdateWorkerDto;
import com.finanacialtracing.dto.worker.WorkerCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCriteria;
import com.finanacialtracing.dto.financialoperation.organization.FinancialOperationCreateDto;
import com.finanacialtracing.dto.financialoperation.FinancialOperationUpdateDto;
import com.finanacialtracing.dto.foType.OrganizationFOTypeCreateDto;
import com.finanacialtracing.dto.foType.OrganizationFOTypeUpdateDto;
import com.finanacialtracing.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.dto.organization.OrganizationUpdateDto;

public interface OrganizationFinancialOperationService {
    CommonResult createOrganization(OrganizationCreateDto organizationCreateDto);
    CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto);
    CommonResult deleteOrganization(Long orgId);
    CommonResult getOrganization();
    CommonResult getMyOrganizations();
    CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto);
    CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto);
    CommonResult deleteWorkerPosition(Long workerPositionId);
    CommonResult getWorkerPositionList(Long orgId);
    CommonResult addWorker(WorkerCreateDto workerCreateDto);
    CommonResult deleteWorker(Long workerId);
    CommonResult editWorkerPositionAndPermission(UpdateWorkerDto updateWorkerDto);
    CommonResult getWorkers(Long orgId);
    CommonResult createOrgFOType(OrganizationFOTypeCreateDto organizationFOTypeCreateDto);
    CommonResult editOrgFOType(OrganizationFOTypeUpdateDto organizationFOTypeUpdateDto);
    CommonResult deleteOrgFOType(Long foTypeId);
    CommonResult getOrgFOType(Long orgId);
    CommonResult createOrgFO(FinancialOperationCreateDto financialOperationCreateDto);
    CommonResult editOrgFO(FinancialOperationUpdateDto financialOperationUpdateDto);
    CommonResult deleteOrgFO(Long foId);
    CommonResult getOrgFO(int size, int page, OrganizationFinancialOperationCriteria organizationFinancialOperationCriteria);
}
