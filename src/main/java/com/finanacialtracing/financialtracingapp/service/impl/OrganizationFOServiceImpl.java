package com.finanacialtracing.financialtracingapp.service.impl;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationDto;
import com.finanacialtracing.financialtracingapp.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionDto;
import com.finanacialtracing.financialtracingapp.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.financialtracingapp.entity.Organization;
import com.finanacialtracing.financialtracingapp.entity.User;
import com.finanacialtracing.financialtracingapp.entity.Worker;
import com.finanacialtracing.financialtracingapp.entity.WorkerPosition;
import com.finanacialtracing.financialtracingapp.exception.Errors;
import com.finanacialtracing.financialtracingapp.exception.GenericException;
import com.finanacialtracing.financialtracingapp.repository.OrganizationRepository;
import com.finanacialtracing.financialtracingapp.repository.WorkerPositionRepository;
import com.finanacialtracing.financialtracingapp.repository.WorkerRepository;
import com.finanacialtracing.financialtracingapp.service.OrganizationFOService;
import com.finanacialtracing.financialtracingapp.util.securityutils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrganizationFOServiceImpl implements OrganizationFOService {

    private final OrganizationRepository organizationRepository;
    private final WorkerPositionRepository workerPositionRepository;
    private final WorkerRepository workerRepository;

    @Override
    public CommonResult createOrganization(OrganizationCreateDto organizationCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = new Organization();
        organization.setOrgName(organizationCreateDto.getOrgName());
        organization.setId(currentUser.getId());
        organization.setIsDeleted(Boolean.FALSE);
        Organization savedOrganization = organizationRepository.save(organization);
        return new CommonResult(savedOrganization.getId() + " is successfully created");
    }

    @Override
    public CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(organizationUpdateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE_ORGANIZATION);
        }
        if (!Objects.equals(organization.getOrgName(), organizationUpdateDto.getOrgName())) {
            organization.setOrgName(organizationUpdateDto.getOrgName());
        }
        Organization updatedOrganization = organizationRepository.save(organization);
        return new CommonResult(updatedOrganization.getId() + " is successfully edited");
    }

    @Override
    public CommonResult deleteOrganization(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE_ORGANIZATION);
        }
        boolean isDeleted = organizationRepository.deleteOrganization(orgId) > 0;
        return new CommonResult(organization.getOrgName() + (isDeleted ? " is successfully  deleted" : " cannot delete"));
    }

    @Override
    public CommonResult getOrganization() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<OrganizationDto> organizationDtoList = new ArrayList<>();
        for (Worker worker : workerRepository.findAllByUserIdAndIsDeleted(currentUser.getId(), Boolean.FALSE)) {
            Organization organization = organizationRepository.findByIdAndIsDeleted(worker.getOrgId(), Boolean.FALSE).orElse(new Organization());
            if (organization.getId() != null) {
                organizationDtoList.add(new OrganizationDto(organization.getId(), organization.getOrgName()));
            }
        }
        return new CommonResult(organizationDtoList);
    }

    @Override
    public CommonResult getMyOrganizations() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<OrganizationDto> organizationDtoList = organizationRepository.findAllByUserIdAndIsDeleted(currentUser.getId(), Boolean.FALSE)
                .stream().map(organization -> new OrganizationDto(organization.getId(), organization.getOrgName()))
                .toList();
        return new CommonResult(organizationDtoList);
    }

    @Override
    public CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(workerPositionCreateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_CREATE_ORGANIZATION_POSITION);
        }
        WorkerPosition workerPosition = new WorkerPosition();
        workerPosition.setOrgId(organization.getId());
        workerPosition.setName(workerPosition.getName());
        workerPosition.setIsDeleted(Boolean.FALSE);
        WorkerPosition savedWorkerPosition = workerPositionRepository.save(workerPosition);
        return new CommonResult(savedWorkerPosition.getName() + " worker position is created");
    }

    @Override
    public CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        WorkerPosition workerPosition = workerPositionRepository.findByIdAndIsDeleted(workerPositionUpdateDto.getWorkerPositionId(), Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(workerPosition.getOrgId(), Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_CREATE_ORGANIZATION_POSITION);
        }
        if (!Objects.equals(workerPosition.getName(), workerPositionUpdateDto.getNewPositionName())) {
            workerPosition.setName(workerPosition.getName());
        }
        WorkerPosition updatedWorkerPosition = workerPositionRepository.save(workerPosition);
        return new CommonResult(updatedWorkerPosition.getName() + " is successfully updated");
    }

    @Override
    public CommonResult deleteWorkerPosition(Long workerPositionId) {
        User currentUser = SecurityUtils.getCurrentUser();
        WorkerPosition workerPosition = workerPositionRepository.findByIdAndIsDeleted(workerPositionId, Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(workerPosition.getOrgId(), Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE_ORGANIZATION_POSITION);
        }
        workerPosition.setIsDeleted(Boolean.TRUE);
        WorkerPosition deletedWorkerPosition = workerPositionRepository.save(workerPosition);
        return new CommonResult(deletedWorkerPosition.getName() + " is Successfully deleted");
    }

    @Override
    public CommonResult getWorkerPositionList(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_GET_WORKER_POSITIONS);
        }
        List<WorkerPositionDto> workerPositionDtoList = workerPositionRepository.findAllByOrgIdAndIsDeleted(orgId, Boolean.FALSE)
                .stream().map(workerPosition -> new WorkerPositionDto(workerPosition.getId(), workerPosition.getName()))
                .toList();
        return new CommonResult(workerPositionDtoList);
    }

    @Override
    public CommonResult addWorker() {
        return null;
    }

    @Override
    public CommonResult deleteWorker() {
        return null;
    }

    @Override
    public CommonResult editWorkerPositionAndPermission() {
        return null;
    }

    @Override
    public CommonResult getWorkers() {
        return null;
    }

    @Override
    public CommonResult createOrgFOType() {
        return null;
    }

    @Override
    public CommonResult editOrgFOType() {
        return null;
    }

    @Override
    public CommonResult deleteOrgFOType() {
        return null;
    }

    @Override
    public CommonResult getOrgFOType() {
        return null;
    }

    @Override
    public CommonResult createOrgFO() {
        return null;
    }

    @Override
    public CommonResult editOrgFO() {
        return null;
    }

    @Override
    public CommonResult deleteOrgFO() {
        return null;
    }

    @Override
    public CommonResult getOrgFO() {
        return null;
    }
}
