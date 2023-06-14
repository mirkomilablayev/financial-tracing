package com.finanacialtracing.service.impl;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.FinancialOperationResponse;
import com.finanacialtracing.dto.transaction.organization.OrganizationFinancialOperationCriteria;
import com.finanacialtracing.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.dto.worker.UpdateWorkerDto;
import com.finanacialtracing.dto.worker.WorkerCreateDto;
import com.finanacialtracing.dto.worker.WorkerDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionCreateDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionDto;
import com.finanacialtracing.dto.workerposition.WorkerPositionUpdateDto;
import com.finanacialtracing.entity.*;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.dto.transaction.organization.TranCreateDto;
import com.finanacialtracing.dto.transaction.FinancialOperationUpdateDto;
import com.finanacialtracing.dto.foType.FOTypeDto;
import com.finanacialtracing.dto.foType.OrganizationFOTypeCreateDto;
import com.finanacialtracing.dto.foType.OrganizationFOTypeUpdateDto;
import com.finanacialtracing.dto.organization.OrganizationDto;
import com.finanacialtracing.service.OrganizationFinancialOperationService;
import com.finanacialtracing.util.constants.WorkerPermissionConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import com.finanacialtracing.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OrganizationFinancialOperationServiceImpl implements OrganizationFinancialOperationService {

    private final OrganizationRepository organizationRepository;
    private final WorkerPositionRepository workerPositionRepository;
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final WorkerPermissionRepository workerPermissionRepository;
    private final FOTypeRepository foTypeRepository;
    private final FinancialOperationRepository financialOperationRepository;

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
    public CommonResult addWorker(WorkerCreateDto workerCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Worker worker = new Worker();
        if (userRepository.existsByIdAndIsDeleted(workerCreateDto.getUserId(), Boolean.FALSE)) {
            throw new GenericException(Errors.NOT_FOUND);
        }
        if (organizationRepository.existsByIdAndIsDeleted(workerCreateDto.getOrgId(), Boolean.FALSE)) {
            throw new GenericException(Errors.NOT_FOUND);
        }
        if (workerRepository.existsByIdAndIsDeleted(workerCreateDto.getPositionId(), Boolean.FALSE)) {
            throw new GenericException(Errors.NOT_FOUND);
        }
        if (Objects.equals(workerCreateDto.getPermissionIds().size(), 0)) {
            throw new GenericException(Errors.CANNOT_CREATE_WORKER_CAUSE_WORKER_PERMISSION);
        }
        if (workerRepository.existsByUserIdAndOrgId(workerCreateDto.getUserId(), workerCreateDto.getOrgId())) {
            throw new GenericException(Errors.USER_ALREADY_ADDED_AS_WORKER);
        }

        worker.setUserId(workerCreateDto.getUserId());
        worker.setOrgId(workerCreateDto.getOrgId());
        worker.setPositionId(workerCreateDto.getPositionId());
        worker.setAddedBy(currentUser.getId());

        Set<WorkerPermission> workerPermissionSet = new HashSet<>();
        for (Long permissionId : workerCreateDto.getPermissionIds()) {
            WorkerPermission workerPermission = workerPermissionRepository.findByIdAndIsDeleted(permissionId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            workerPermissionSet.add(workerPermission);
        }

        worker.setPermissions(workerPermissionSet);
        Worker savedWorker = workerRepository.save(worker);
        return new CommonResult(savedWorker.getId() + " is successfully deleted");
    }

    @Override
    public CommonResult deleteWorker(Long workerId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Worker worker = workerRepository.findByIdAndIsDeleted(workerId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(worker.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE_WORKER);
        }
        worker.setIsDeleted(Boolean.TRUE);
        Worker deletedWorker = workerRepository.save(worker);
        return new CommonResult(deletedWorker.getId() + " is successfully deleted!");
    }

    @Override
    public CommonResult editWorkerPositionAndPermission(UpdateWorkerDto updateWorkerDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Worker worker = workerRepository.findByIdAndIsDeleted(updateWorkerDto.getWorkerId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(worker.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE_WORKER);
        }

        if (Objects.nonNull(updateWorkerDto.getWorkerId()) && !Objects.equals(worker.getPositionId(), updateWorkerDto.getNewPositionId())) {
            WorkerPosition workerPosition = workerPositionRepository.findByIdAndIsDeleted(updateWorkerDto.getWorkerId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            worker.setPositionId(workerPosition.getId());
        }
        if (!Objects.equals(updateWorkerDto.getNewPermissions().size(), 0)) {
            Set<WorkerPermission> workerPermissionSet = new HashSet<>();
            for (Long permissionId : updateWorkerDto.getNewPermissions()) {
                WorkerPermission workerPermission = workerPermissionRepository.findByIdAndIsDeleted(permissionId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
                workerPermissionSet.add(workerPermission);
            }
            worker.setPermissions(workerPermissionSet);
        }
        Worker updateWorker = workerRepository.save(worker);
        return new CommonResult(updateWorker.getId() + " is Successfully updated");
    }

    @Override
    public CommonResult getWorkers(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId()) ||
                !workerRepository.existsByUserIdAndOrgIdAndIsDeleted(currentUser.getId(), orgId, Boolean.FALSE)) {
            throw new GenericException(Errors.CANNOT_GET_WORKER);
        }
        List<WorkerDto> workerDtoList = workerRepository.findAllByOrgIdAndIsDeleted(orgId, Boolean.FALSE)
                .stream()
                .map(worker -> {
                    WorkerPosition workerPosition = workerPositionRepository.findByIdAndIsDeleted(worker.getPositionId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
                    return new WorkerDto(
                            worker.getId(),
                            currentUser.getId(),
                            currentUser.getUsername(),
                            currentUser.getFullName(),
                            workerPosition.getId(),
                            workerPosition.getName()
                    );
                }).toList();

        return new CommonResult(workerDtoList);
    }

    @Override
    public CommonResult createOrgFOType(OrganizationFOTypeCreateDto organizationFOTypeCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(organizationFOTypeCreateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_CREATE_FO_TYPE);
        }
        TransactionType transactionType = new TransactionType();
        transactionType.setOrgId(organizationFOTypeCreateDto.getOrgId());
        transactionType.setIsPersonal(Boolean.FALSE);
        transactionType.setIsDeleted(Boolean.FALSE);
        transactionType.setUserId(currentUser.getId());
        transactionType.setName(organizationFOTypeCreateDto.getName());
        TransactionType savedTransactionType = foTypeRepository.save(transactionType);
        return new CommonResult(savedTransactionType.getName() + " is successfully created");
    }

    @Override
    public CommonResult editOrgFOType(OrganizationFOTypeUpdateDto organizationFOTypeUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(organizationFOTypeUpdateDto.getFoTypeId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transactionType.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE_FO_TYPE);
        }
        if (!Objects.equals(transactionType.getName(), organizationFOTypeUpdateDto.getNewName())) {
            transactionType.setName(organizationFOTypeUpdateDto.getNewName());
        }
        TransactionType savedTransactionType = foTypeRepository.save(transactionType);
        return new CommonResult("Financial operation type is updated to " + savedTransactionType.getName());
    }

    @Override
    public CommonResult deleteOrgFOType(Long foTypeId) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(foTypeId, Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transactionType.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE_FO_TYPE);
        }
        if (financialOperationRepository.existsByTypeIdAndIsDeletedAndIsPersonal(foTypeId, Boolean.FALSE, Boolean.FALSE)) {
            throw new GenericException(Errors.CANNOT_DELETE_FO_TYPE);
        }

        transactionType.setIsDeleted(Boolean.TRUE);
        TransactionType deletedTransactionType = foTypeRepository.save(transactionType);
        return new CommonResult(deletedTransactionType.getName() + " is successfully deleted!");
    }

    @Override
    public CommonResult getOrgFOType(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_GET_FO_TYPE_LIST);
        }
        List<FOTypeDto> foTypeDtoList = foTypeRepository.findAllByOrgIdAndIsDeletedAndIsPersonal(organization.getId(), Boolean.FALSE, Boolean.FALSE)
                .stream().map(transactionType -> new FOTypeDto(
                        transactionType.getId(),
                        transactionType.getName()
                )).toList();
        return new CommonResult(foTypeDtoList);
    }

    @Override
    public CommonResult createOrgFO(TranCreateDto tranCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(tranCreateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(tranCreateDto.getFoTypeId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_add_fo);

        Transaction transaction = new Transaction();
        transaction.setTypeId(transactionType.getId());
        transaction.setIsDeleted(Boolean.FALSE);
        transaction.setIsPersonal(Boolean.FALSE);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAmount(tranCreateDto.getAmount());
        transaction.setOrgId(organization.getId());
        transaction.setIsIncome(tranCreateDto.getIsIncome());
        transaction.setDescription(tranCreateDto.getDescription());
        Transaction savedFO = financialOperationRepository.save(transaction);
        return new CommonResult(savedFO.getId() + " is successfully saved");
    }

    private Worker getWorker(Long currentUserId, Long orgId) {
        return workerRepository.findByUserIdAndOrgIdAndIsDeleted(currentUserId, orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.YOU_CANNOT_ADD_FINANCIAL_OPERATION));
    }

    @Override
    public CommonResult editOrgFO(FinancialOperationUpdateDto financialOperationUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = financialOperationRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationUpdateDto.getId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transaction.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_edit_fo);


        if (Objects.equals(transaction.getAmount(), financialOperationUpdateDto.getAmount())) {
            transaction.setAmount(financialOperationUpdateDto.getAmount());
        }
        if (Objects.equals(transaction.getDescription(), financialOperationUpdateDto.getDescription())) {
            transaction.setDescription(financialOperationUpdateDto.getDescription());
        }
        if (Objects.equals(transaction.getTypeId(), financialOperationUpdateDto.getFoTypeId())) {
            TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationUpdateDto.getFoTypeId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            if (!Objects.equals(transaction.getOrgId(), transactionType.getOrgId())) {
                throw new GenericException(Errors.CANNOT_UPDATE_FO_TYPE);
            }
            transaction.setTypeId(financialOperationUpdateDto.getFoTypeId());
        }
        if (Objects.equals(transaction.getIsIncome(), financialOperationUpdateDto.getIsIncome())) {
            transaction.setIsIncome(financialOperationUpdateDto.getIsIncome());
        }


        Transaction savedTransaction = financialOperationRepository.save(transaction);
        return new CommonResult(savedTransaction.getId() + " is successfully updated");
    }


    @Override
    public CommonResult deleteOrgFO(Long financialOperationId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = financialOperationRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationId, Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transaction.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_delete_fo);
        transaction.setIsDeleted(Boolean.TRUE);
        Transaction deletedTransaction = financialOperationRepository.save(transaction);
        return new CommonResult(deletedTransaction.getId() + " is successfully deleted!");
    }

    @Override
    public CommonResult getOrgFO(int size, int page, OrganizationFinancialOperationCriteria organizationFinancialOperationCriteria) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(organizationFinancialOperationCriteria.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_delete_fo);

        FinancialOperationResponse financialOperationResponse = new FinancialOperationResponse();
        financialOperationResponse.setIncome(new ArrayList<>());
        financialOperationResponse.setOutcome(new ArrayList<>());

        List<Transaction> financialOperationsList = financialOperationRepository.getOrganizationFinancialOperationsList(
                organizationFinancialOperationCriteria.getSortedByAmount(),
                organizationFinancialOperationCriteria.getMinAmount(),
                organizationFinancialOperationCriteria.getMaxAmount(),
                organizationFinancialOperationCriteria.getSortedByFoType(),
                organizationFinancialOperationCriteria.getFoTypeId(),
                organizationFinancialOperationCriteria.getOrgId(),
                size,
                page
        );
        for (Transaction transaction : financialOperationsList) {
            if (organizationFinancialOperationCriteria.getSortedByDate()) {
                if (
                        transaction.getCreatedAt().isBefore(organizationFinancialOperationCriteria.getFromDate()) &&
                                transaction.getCreatedAt().isAfter(organizationFinancialOperationCriteria.getToDate())
                ) {
                    continue;
                }
            }
            if (transaction.getIsIncome()) {
                financialOperationResponse.getIncome().add(transaction);
            } else {
                financialOperationResponse.getOutcome().add(transaction);
            }
        }
        return new CommonResult(financialOperationResponse, Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }


    private void checkWorkerPermission(User currentUser, Organization organization, String workerPermission) {
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            Worker worker = getWorker(currentUser.getId(), organization.getId());
            boolean flag = false;
            for (WorkerPermission permission : worker.getPermissions()) {
                if (permission.getName().equals(workerPermission)) {
                    flag = true;
                }
            }
            if (!flag) {
                throw new GenericException(Errors.YOU_CANNOT_ADD_FINANCIAL_OPERATION);
            }
        }
    }
}
