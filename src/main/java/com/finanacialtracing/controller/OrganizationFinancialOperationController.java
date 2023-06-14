package com.finanacialtracing.controller;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.organization.OrganizationFinancialOperationCriteria;
import com.finanacialtracing.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.dto.worker.UpdateWorkerDto;
import com.finanacialtracing.dto.worker.WorkerCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.dto.transaction.FinancialOperationUpdateDto;
import com.finanacialtracing.dto.transaction.organization.TranCreateDto;
import com.finanacialtracing.dto.foType.OrganizationFOTypeCreateDto;
import com.finanacialtracing.dto.foType.OrganizationFOTypeUpdateDto;
import com.finanacialtracing.service.OrganizationFinancialOperationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
@AllArgsConstructor
@Slf4j
public class OrganizationFinancialOperationController {
    private final OrganizationFinancialOperationService OrganizationFinancialOperationService;

    @PostMapping("/create-organization")
    public CommonResult createOrganization(OrganizationCreateDto organizationCreateDto) {
        log.info("Rest request to createOrganization()");
        return OrganizationFinancialOperationService.createOrganization(organizationCreateDto);
    }

    @PutMapping("/update-organization")
    public CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto) {
        log.info("Rest request to updateOrganization()");
        return OrganizationFinancialOperationService.updateOrganization(organizationUpdateDto);
    }

    @DeleteMapping("/delete-organization")
    public CommonResult deleteOrganization(Long orgId) {
        log.info("Rest request to deleteOrganization()");
        return OrganizationFinancialOperationService.deleteOrganization(orgId);
    }

    @GetMapping("/get-organization")
    public CommonResult getOrganization() {
        log.info("Rest request to getOrganization()");
        return OrganizationFinancialOperationService.getOrganization();
    }

    @GetMapping("/get-my-organization")
    public CommonResult getMyOrganizations() {
        log.info("Rest request to getMyOrganizations()");
        return OrganizationFinancialOperationService.getMyOrganizations();
    }

    @PostMapping("/add-worker-position")
    public CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto) {
        log.info("Rest request to addWorkerPosition()");
        return OrganizationFinancialOperationService.addWorkerPosition(workerPositionCreateDto);
    }

    @PutMapping("/update-worker-position")
    public CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto) {
        log.info("Rest request to editWorkerPosition()");
        return OrganizationFinancialOperationService.editWorkerPosition(workerPositionUpdateDto);
    }

    @DeleteMapping("/delete-worker-positin")
    public CommonResult deleteWorkerPosition(Long workerPositionId) {
        log.info("Rest request to deleteWorkerPosition()");
        return OrganizationFinancialOperationService.deleteWorkerPosition(workerPositionId);
    }

    @GetMapping("/get-worker-position-list")
    public CommonResult getWorkerPositionList(Long orgId) {
        log.info("Rest request to getWorkerPositionList()");
        return OrganizationFinancialOperationService.getWorkerPositionList(orgId);
    }

    @PostMapping("/add-worker")
    public CommonResult addWorker(@RequestBody WorkerCreateDto workerCreateDto) {
        return OrganizationFinancialOperationService.addWorker(workerCreateDto);
    }

    @DeleteMapping("/delete-worker/{workerId}")
    public CommonResult deleteWorker(@PathVariable Long workerId) {
        return OrganizationFinancialOperationService.deleteWorker(workerId);
    }

    @PutMapping("/update-worker")
    public CommonResult editWorkerPositionAndPermission(@RequestBody UpdateWorkerDto updateWorkerDto) {
        return OrganizationFinancialOperationService.editWorkerPositionAndPermission(updateWorkerDto);
    }

    @GetMapping("/get-workers/{orgId}")
    public CommonResult getWorkers(@PathVariable Long orgId) {
        return OrganizationFinancialOperationService.getWorkers(orgId);
    }

    @PostMapping("/create-org-fo-type")
    public CommonResult createOrgFOType(@RequestBody OrganizationFOTypeCreateDto organizationFOTypeCreateDto) {
        return OrganizationFinancialOperationService.createOrgFOType(organizationFOTypeCreateDto);
    }


    @PutMapping("/edit-org-fo-type")
    public CommonResult editOrgFOType(OrganizationFOTypeUpdateDto organizationFOTypeUpdateDto) {
        return OrganizationFinancialOperationService.editOrgFOType(organizationFOTypeUpdateDto);
    }

    @DeleteMapping("/delete-org-fo-type/{foTypeId}")
    public CommonResult deleteOrgFOType(@PathVariable Long foTypeId) {
        return OrganizationFinancialOperationService.deleteOrgFOType(foTypeId);
    }

    @GetMapping("/get-org-fo-types/{orgId}")
    public CommonResult getOrgFOType(@PathVariable Long orgId) {
        return OrganizationFinancialOperationService.getOrgFOType(orgId);
    }

    @PostMapping("/create-org-fo")
    public CommonResult createOrgFO(@RequestBody TranCreateDto tranCreateDto) {
        log.info("Rest request to createOrgFO()");
        return OrganizationFinancialOperationService.createOrgFO(tranCreateDto);
    }

    @PutMapping("/edit-org-fo")
    public CommonResult editOrgFO(@RequestBody FinancialOperationUpdateDto financialOperationUpdateDto) {
        log.info("Rest request to editOrgFO()");
        return OrganizationFinancialOperationService.editOrgFO(financialOperationUpdateDto);
    }

    @DeleteMapping("/delete-org-fo/{foId}")
    public CommonResult deleteOrgFO(@PathVariable Long foId) {
        log.info("Rest request to deleteOrgFO()");
        return OrganizationFinancialOperationService.deleteOrgFO(foId);
    }

    @GetMapping("/get-org-fo-list")
    public CommonResult getOrgFO(@RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestBody OrganizationFinancialOperationCriteria organizationFinancialOperationCriteria) {
        log.info("Rest request to getOrgFO()");
        return OrganizationFinancialOperationService.getOrgFO(size, page, organizationFinancialOperationCriteria);
    }
}
