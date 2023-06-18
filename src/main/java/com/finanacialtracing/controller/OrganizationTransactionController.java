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

    /**
     *  create organization
     */
    @PostMapping("/create-organization")
    public CommonResult createOrganization(OrganizationCreateDto organizationCreateDto) {
        log.info("Rest request to createOrganization()");
        return organizationTransactionService.createOrganization(organizationCreateDto);
    }

    /**
     *update organization
     */
    @PutMapping("/update-organization")
    public CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto) {
        log.info("Rest request to updateOrganization()");
        return organizationTransactionService.updateOrganization(organizationUpdateDto);
    }

    /**
     * delete organization by id
     */
    @DeleteMapping("/delete-organization/{orgId}")
    public CommonResult deleteOrganization(@PathVariable Long orgId) {
        log.info("Rest request to deleteOrganization()");
        return organizationTransactionService.deleteOrganization(orgId);
    }

    /**
     * get organizations that i worked
     */
    @GetMapping("/get-organizations")
    public CommonResult getOrganization() {
        log.info("Rest request to getOrganization()");
        return organizationTransactionService.getOrganizationIAmWorking();
    }

    /**
     * get organizations that i created
     */
    @GetMapping("/get-my-organization")
    public CommonResult getMyOrganizations() {
        log.info("Rest request to getMyOrganizations()");
        return organizationTransactionService.getMyOrganizations();
    }


    /**
     * create worker position
     */
    @PostMapping("/add-worker-position")
    public CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto) {
        log.info("Rest request to addWorkerPosition()");
        return organizationTransactionService.addWorkerPosition(workerPositionCreateDto);
    }

    /**
     * update worker position
     */
    @PutMapping("/update-worker-position")
    public CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto) {
        log.info("Rest request to editWorkerPosition()");
        return organizationTransactionService.editWorkerPosition(workerPositionUpdateDto);
    }

    /**
     * delete worker position
     */
    @DeleteMapping("/delete-worker-position/{workerPositionId}")
    public CommonResult deleteWorkerPosition(@PathVariable Long workerPositionId) {
        log.info("Rest request to deleteWorkerPosition()");
        return organizationTransactionService.deleteWorkerPosition(workerPositionId);
    }

    /**
     * get all worker position
     */
    @GetMapping("/get-worker-position-list")
    public CommonResult getWorkerPositionList(Long orgId) {
        log.info("Rest request to getWorkerPositionList()");
        return organizationTransactionService.getWorkerPositionList(orgId);
    }

    /**
     * add worker
     */
    @PostMapping("/add-worker")
    public CommonResult addWorker(@RequestBody WorkerCreateDto workerCreateDto) {
        return organizationTransactionService.addWorker(workerCreateDto);
    }

    /**
     * delete worker by idr
     */
    @DeleteMapping("/delete-worker/{workerId}")
    public CommonResult deleteWorker(@PathVariable Long workerId) {
        return organizationTransactionService.deleteWorker(workerId);
    }

    /**
     * update worker
     */
    @PutMapping("/update-worker")
    public CommonResult editWorkerPositionAndPermission(@RequestBody UpdateWorkerDto updateWorkerDto) {
        return organizationTransactionService.editWorkerPositionAndPermission(updateWorkerDto);
    }

    /**
     * get worker lists by organization id
     */
    @GetMapping("/get-workers/{orgId}")
    public CommonResult getWorkers(@PathVariable Long orgId) {
        return organizationTransactionService.getWorkers(orgId);
    }

    /**
     * create transaction type for organization
     */
    @PostMapping("/create-org-tran-type")
    public CommonResult createOrganizationTransactionType(@RequestBody OrganizationTransactionTypeCreateDto organizationTransactionTypeCreateDto) {
        return organizationTransactionService.createOrganizationTransactionType(organizationTransactionTypeCreateDto);
    }

    /**
     * update transaction type for organization
     */
    @PutMapping("/update-org-tran-type")
    public CommonResult updateOrganizationTransactionType(OrganizationTransactionTypeUpdateDto organizationTransactionTypeUpdateDto) {
        return organizationTransactionService.updateOrganizationTransactionType(organizationTransactionTypeUpdateDto);
    }

    /**
     * delete transaction type for organization  by id
     */
    @DeleteMapping("/delete-org-tran-type/{tranTypeId}")
    public CommonResult deleteOrganizationTransactionType(@PathVariable Long tranTypeId) {
        log.info("Rest request to deleteOrganizationTransactionType()");
        return organizationTransactionService.deleteOrganizationTransactionType(tranTypeId);
    }

    /**
     * get transaction type list of organization
     */
    @GetMapping("/get-org-tran-types/{orgId}")
    public CommonResult getOrganizationTransactionType(@PathVariable Long orgId) {
        log.info("Rest request to getOrganizationTransactionType()");
        return organizationTransactionService.getOrganizationTransactionType(orgId);
    }

    /**
     * create transaction for organization
     */
    @PostMapping("/create-org-transaction")
    public CommonResult createOrganizationTransaction(@RequestBody TranCreateDto tranCreateDto) {
        log.info("Rest request to createOrgFO()");
        return organizationTransactionService.createOrganizationTransaction(tranCreateDto);
    }
    /**
     * update transaction for organization
     */
    @PutMapping("/update-org-transaction")
    public CommonResult updateOrganizationTransaction(@RequestBody TransactionUpdateDto transactionUpdateDto) {
        log.info("Rest request to updateOrganizationTransaction()");
        return organizationTransactionService.updateOrganizationTransaction(transactionUpdateDto);
    }

    /**
     * delete transaction for organization by id
     */
    @DeleteMapping("/delete-org-transaction/{tranId}")
    public CommonResult deleteOrganizationTransaction(@PathVariable Long tranId) {
        log.info("Rest request to deleteOrgFO()");
        return organizationTransactionService.deleteOrganizationTransaction(tranId);
    }

    /**
     * get transactions for organization by filtering
     */
    @GetMapping("/get-org-transaction-list")
    public CommonResult getOrganizationTransactionList(@RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestBody OrganizationTranCriteria organizationTranCriteria) {
        log.info("Rest request to getOrgFO()");
        return organizationTransactionService.getOrganizationTransactionList(size, page, organizationTranCriteria);
    }
}
