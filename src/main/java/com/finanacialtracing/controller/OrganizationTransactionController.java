package com.finanacialtracing.controller;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.organization.OrganizationTranCriteria;
import com.finanacialtracing.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.dto.worker.UpdateWorkerDto;
import com.finanacialtracing.dto.worker.WorkerCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transaction.organization.TranCreateDto;
import com.finanacialtracing.dto.transactiontype.OrganizationTransactionTypeCreateDto;
import com.finanacialtracing.dto.transactiontype.OrganizationTransactionTypeUpdateDto;
import com.finanacialtracing.service.OrganizationTransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
@AllArgsConstructor
@Slf4j
public class OrganizationTransactionController {
    private final OrganizationTransactionService organizationTransactionService;


    @PostMapping("/create-organization")
    public CommonResult createOrganization(OrganizationCreateDto organizationCreateDto) {
        log.info("Rest request to createOrganization()");
        return organizationTransactionService.createOrganization(organizationCreateDto);
    }

    @PutMapping("/update-organization")
    public CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto) {
        log.info("Rest request to updateOrganization()");
        return organizationTransactionService.updateOrganization(organizationUpdateDto);
    }

    @DeleteMapping("/delete-organization")
    public CommonResult deleteOrganization(Long orgId) {
        log.info("Rest request to deleteOrganization()");
        return organizationTransactionService.deleteOrganization(orgId);
    }

    @GetMapping("/get-organization")
    public CommonResult getOrganization() {
        log.info("Rest request to getOrganization()");
        return organizationTransactionService.getOrganizationIAmWorking();
    }

    @GetMapping("/get-my-organization")
    public CommonResult getMyOrganizations() {
        log.info("Rest request to getMyOrganizations()");
        return organizationTransactionService.getMyOrganizations();
    }

    @PostMapping("/add-worker-position")
    public CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto) {
        log.info("Rest request to addWorkerPosition()");
        return organizationTransactionService.addWorkerPosition(workerPositionCreateDto);
    }

    @PutMapping("/update-worker-position")
    public CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto) {
        log.info("Rest request to editWorkerPosition()");
        return organizationTransactionService.editWorkerPosition(workerPositionUpdateDto);
    }

    @DeleteMapping("/delete-worker-positin")
    public CommonResult deleteWorkerPosition(Long workerPositionId) {
        log.info("Rest request to deleteWorkerPosition()");
        return organizationTransactionService.deleteWorkerPosition(workerPositionId);
    }

    @GetMapping("/get-worker-position-list")
    public CommonResult getWorkerPositionList(Long orgId) {
        log.info("Rest request to getWorkerPositionList()");
        return organizationTransactionService.getWorkerPositionList(orgId);
    }

    @PostMapping("/add-worker")
    public CommonResult addWorker(@RequestBody WorkerCreateDto workerCreateDto) {
        return organizationTransactionService.addWorker(workerCreateDto);
    }

    @DeleteMapping("/delete-worker/{workerId}")
    public CommonResult deleteWorker(@PathVariable Long workerId) {
        return organizationTransactionService.deleteWorker(workerId);
    }

    @PutMapping("/update-worker")
    public CommonResult editWorkerPositionAndPermission(@RequestBody UpdateWorkerDto updateWorkerDto) {
        return organizationTransactionService.editWorkerPositionAndPermission(updateWorkerDto);
    }

    @GetMapping("/get-workers/{orgId}")
    public CommonResult getWorkers(@PathVariable Long orgId) {
        return organizationTransactionService.getWorkers(orgId);
    }

    @PostMapping("/create-org-tran-type")
    public CommonResult createOrganizationTransactionType(@RequestBody OrganizationTransactionTypeCreateDto organizationTransactionTypeCreateDto) {
        return organizationTransactionService.createOrganizationTransactionType(organizationTransactionTypeCreateDto);
    }


    @PutMapping("/edit-org-tran-type")
    public CommonResult editOrganizationTransactionType(OrganizationTransactionTypeUpdateDto organizationTransactionTypeUpdateDto) {
        return organizationTransactionService.editOrganizationTransactionType(organizationTransactionTypeUpdateDto);
    }

    @DeleteMapping("/delete-org-tran-type/{tranTypeId}")
    public CommonResult deleteOrganizationTransactionType(@PathVariable Long tranTypeId) {
        return organizationTransactionService.deleteOrganizationTransactionType(tranTypeId);
    }

    @GetMapping("/get-org-tran-types/{orgId}")
    public CommonResult getOrganizationTransactionType(@PathVariable Long orgId) {
        return organizationTransactionService.getOrganizationTransactionType(orgId);
    }

    @PostMapping("/create-org-transaction")
    public CommonResult createOrganizationTransaction(@RequestBody TranCreateDto tranCreateDto) {
        log.info("Rest request to createOrgFO()");
        return organizationTransactionService.createOrganizationTransaction(tranCreateDto);
    }

    @PutMapping("/edit-org-transaction")
    public CommonResult editOrganizationTransaction(@RequestBody TransactionUpdateDto transactionUpdateDto) {
        log.info("Rest request to editOrgFO()");
        return organizationTransactionService.editOrganizationTransaction(transactionUpdateDto);
    }

    @DeleteMapping("/delete-org-transaction/{tranId}")
    public CommonResult deleteOrganizationTransaction(@PathVariable Long tranId) {
        log.info("Rest request to deleteOrgFO()");
        return organizationTransactionService.deleteOrganizationTransaction(tranId);
    }

    @GetMapping("/get-org-transaction-list")
    public CommonResult getOrganizationTransactionList(@RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestBody OrganizationTranCriteria organizationTranCriteria) {
        log.info("Rest request to getOrgFO()");
        return organizationTransactionService.getOrganizationTransactionList(size, page, organizationTranCriteria);
    }
}
