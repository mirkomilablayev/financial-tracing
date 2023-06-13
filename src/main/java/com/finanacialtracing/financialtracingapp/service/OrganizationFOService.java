package com.finanacialtracing.financialtracingapp.service;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.financialtracingapp.dto.worker.WorkerCreateDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionUpdateDto;

public interface OrganizationFOService {
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
    CommonResult editWorkerPositionAndPermission();
    CommonResult getWorkers();
    CommonResult createOrgFOType();
    CommonResult editOrgFOType();
    CommonResult deleteOrgFOType();
    CommonResult getOrgFOType();
    CommonResult createOrgFO();
    CommonResult editOrgFO();
    CommonResult deleteOrgFO();
    CommonResult getOrgFO();
}
