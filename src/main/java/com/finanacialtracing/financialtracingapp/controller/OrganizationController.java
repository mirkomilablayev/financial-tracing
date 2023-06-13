package com.finanacialtracing.financialtracingapp.controller;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.financialtracingapp.service.OrganizationFOService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
@AllArgsConstructor
public class OrganizationController {
    private final OrganizationFOService organizationFOService;

@PostMapping("/create-organization")
    public CommonResult createOrganization(OrganizationCreateDto organizationCreateDto){
        return organizationFOService.createOrganization(organizationCreateDto);
    }
    @PutMapping("/update-organization")
    public CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto){
        return organizationFOService.updateOrganization(organizationUpdateDto);
    }
    @DeleteMapping("/delete-organization")
    public CommonResult deleteOrganization(Long orgId){
        return organizationFOService.deleteOrganization(orgId);
    }
    @GetMapping("/get-organization")
    public CommonResult getOrganization(){
        return organizationFOService.getOrganization();
    }
    @GetMapping("/get-my-organization")
    public CommonResult getMyOrganizations(){
        return organizationFOService.getMyOrganizations();
    }
    @PostMapping("/add-worker-position")
    public CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto){
        return organizationFOService.addWorkerPosition(workerPositionCreateDto);
    }
    @PutMapping("/update-worker-position")
    public CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto){
        return organizationFOService.editWorkerPosition(workerPositionUpdateDto);
    }
    @DeleteMapping("/delete-worker-positin")
    public CommonResult deleteWorkerPosition(Long workerPositionId){
        return organizationFOService.deleteWorkerPosition(workerPositionId);
    }
    @GetMapping("/get-worker-position-list")
    public CommonResult getWorkerPositionList(Long orgId){
        return organizationFOService.getWorkerPositionList(orgId);
    }
}
