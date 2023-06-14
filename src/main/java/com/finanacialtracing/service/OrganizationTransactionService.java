package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.organization.OrganizationTranCriteria;
import com.finanacialtracing.dto.worker.UpdateWorkerDto;
import com.finanacialtracing.dto.worker.WorkerCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.dto.transaction.organization.TranCreateDto;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transactiontype.OrganizationTransactionTypeCreateDto;
import com.finanacialtracing.dto.transactiontype.OrganizationTransactionTypeUpdateDto;
import com.finanacialtracing.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.dto.organization.OrganizationUpdateDto;

public interface OrganizationTransactionService {
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
    CommonResult createOrganizationTransactionType(OrganizationTransactionTypeCreateDto organizationTransactionTypeCreateDto);
    CommonResult editOrganizationTransactionType(OrganizationTransactionTypeUpdateDto organizationTransactionTypeUpdateDto);
    CommonResult deleteOrganizationTransactionType(Long tranTypeId);
    CommonResult getOrganizationTransactionType(Long orgId);
    CommonResult createOrganizationTransaction(TranCreateDto tranCreateDto);
    CommonResult editOrganizationTransaction(TransactionUpdateDto transactionUpdateDto);
    CommonResult deleteOrganizationTransaction(Long TransactionId);
    CommonResult getOrganizationTransactionList(int size, int page, OrganizationTranCriteria organizationTranCriteria);
}
