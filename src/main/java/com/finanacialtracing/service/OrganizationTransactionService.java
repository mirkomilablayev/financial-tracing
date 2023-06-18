package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.TransactionResponse;
import com.finanacialtracing.dto.transaction.organization.OrganizationTranCriteria;
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
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transactiontype.TransactionTypeDto;
import com.finanacialtracing.dto.transactiontype.OrganizationTransactionTypeCreateDto;
import com.finanacialtracing.dto.transactiontype.OrganizationTransactionTypeUpdateDto;
import com.finanacialtracing.dto.organization.OrganizationDto;
import com.finanacialtracing.util.constants.WorkerPermissionConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import com.finanacialtracing.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OrganizationTransactionService  {

    private final OrganizationRepository organizationRepository;
    private final WorkerPositionRepository workerPositionRepository;
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final WorkerPermissionRepository workerPermissionRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionRepository transactionRepository;

    public CommonResult createOrganization(OrganizationCreateDto organizationCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = new Organization();
        organization.setOrgName(organizationCreateDto.getOrgName());
        organization.setId(currentUser.getId());
        organization.setIsDeleted(Boolean.FALSE);
        Organization savedOrganization = organizationRepository.save(organization);
        return new CommonResult(savedOrganization.getId() + " is successfully created");
    }

    public CommonResult updateOrganization(OrganizationUpdateDto organizationUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(organizationUpdateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE);
        }
        if (!Objects.equals(organization.getOrgName(), organizationUpdateDto.getOrgName())) {
            organization.setOrgName(organizationUpdateDto.getOrgName());
        }
        Organization updatedOrganization = organizationRepository.save(organization);
        return new CommonResult(updatedOrganization.getId() + " is successfully edited");
    }

    public CommonResult deleteOrganization(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        boolean isDeleted = organizationRepository.deleteOrganization(orgId) > 0;
        return new CommonResult(organization.getOrgName() + (isDeleted ? " is successfully  deleted" : " cannot delete"));
    }

    public CommonResult getOrganizationIAmWorking() {
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

    public CommonResult getMyOrganizations() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<OrganizationDto> organizationDtoList = organizationRepository.findAllByUserIdAndIsDeleted(currentUser.getId(), Boolean.FALSE)
                .stream().map(organization -> new OrganizationDto(organization.getId(), organization.getOrgName()))
                .toList();
        return new CommonResult(organizationDtoList);
    }

    public CommonResult addWorkerPosition(WorkerPositionCreateDto workerPositionCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(workerPositionCreateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_CREATE);
        }
        WorkerPosition workerPosition = new WorkerPosition();
        workerPosition.setOrgId(organization.getId());
        workerPosition.setName(workerPosition.getName());
        workerPosition.setIsDeleted(Boolean.FALSE);
        WorkerPosition savedWorkerPosition = workerPositionRepository.save(workerPosition);
        return new CommonResult(savedWorkerPosition.getName() + " worker position is created");
    }

    public CommonResult editWorkerPosition(WorkerPositionUpdateDto workerPositionUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        WorkerPosition workerPosition = workerPositionRepository.findByIdAndIsDeleted(workerPositionUpdateDto.getWorkerPositionId(), Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(workerPosition.getOrgId(), Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_CREATE);
        }
        if (!Objects.equals(workerPosition.getName(), workerPositionUpdateDto.getNewPositionName())) {
            workerPosition.setName(workerPosition.getName());
        }
        WorkerPosition updatedWorkerPosition = workerPositionRepository.save(workerPosition);
        return new CommonResult(updatedWorkerPosition.getName() + " is successfully updated");
    }

    public CommonResult deleteWorkerPosition(Long workerPositionId) {
        User currentUser = SecurityUtils.getCurrentUser();
        WorkerPosition workerPosition = workerPositionRepository.findByIdAndIsDeleted(workerPositionId, Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(workerPosition.getOrgId(), Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        workerPosition.setIsDeleted(Boolean.TRUE);
        WorkerPosition deletedWorkerPosition = workerPositionRepository.save(workerPosition);
        return new CommonResult(deletedWorkerPosition.getName() + " is Successfully deleted");
    }

    public CommonResult getWorkerPositionList(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE)
                .orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_GET);
        }
        List<WorkerPositionDto> workerPositionDtoList = workerPositionRepository.findAllByOrgIdAndIsDeleted(orgId, Boolean.FALSE)
                .stream().map(workerPosition -> new WorkerPositionDto(workerPosition.getId(), workerPosition.getName()))
                .toList();
        return new CommonResult(workerPositionDtoList);
    }

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
            throw new GenericException(Errors.CANNOT_CREATE);
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

    public CommonResult deleteWorker(Long workerId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Worker worker = workerRepository.findByIdAndIsDeleted(workerId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(worker.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        worker.setIsDeleted(Boolean.TRUE);
        Worker deletedWorker = workerRepository.save(worker);
        return new CommonResult(deletedWorker.getId() + " is successfully deleted!");
    }

    public CommonResult editWorkerPositionAndPermission(UpdateWorkerDto updateWorkerDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Worker worker = workerRepository.findByIdAndIsDeleted(updateWorkerDto.getWorkerId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(worker.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE);
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

    public CommonResult getWorkers(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId()) ||
                !workerRepository.existsByUserIdAndOrgIdAndIsDeleted(currentUser.getId(), orgId, Boolean.FALSE)) {
            throw new GenericException(Errors.CANNOT_GET);
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

    public CommonResult createOrganizationTransactionType(OrganizationTransactionTypeCreateDto organizationTransactionTypeCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(organizationTransactionTypeCreateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_CREATE);
        }
        TransactionType transactionType = new TransactionType();
        transactionType.setOrgId(organizationTransactionTypeCreateDto.getOrgId());
        transactionType.setIsPersonal(Boolean.FALSE);
        transactionType.setIsDeleted(Boolean.FALSE);
        transactionType.setUserId(currentUser.getId());
        transactionType.setName(organizationTransactionTypeCreateDto.getName());
        TransactionType savedTransactionType = transactionTypeRepository.save(transactionType);
        return new CommonResult(savedTransactionType.getName() + " is successfully created");
    }

    public CommonResult editOrganizationTransactionType(OrganizationTransactionTypeUpdateDto organizationTransactionTypeUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(organizationTransactionTypeUpdateDto.getTransactionTypeId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transactionType.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE);
        }
        if (!Objects.equals(transactionType.getName(), organizationTransactionTypeUpdateDto.getNewName())) {
            transactionType.setName(organizationTransactionTypeUpdateDto.getNewName());
        }
        TransactionType savedTransactionType = transactionTypeRepository.save(transactionType);
        return new CommonResult("Financial operation type is updated to " + savedTransactionType.getName());
    }

    public CommonResult deleteOrganizationTransactionType(Long transactionTypeId) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(transactionTypeId, Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transactionType.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        if (transactionRepository.existsByTypeIdAndIsDeletedAndIsPersonal(transactionTypeId, Boolean.FALSE, Boolean.FALSE)) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }

        transactionType.setIsDeleted(Boolean.TRUE);
        TransactionType deletedTransactionType = transactionTypeRepository.save(transactionType);
        return new CommonResult(deletedTransactionType.getName() + " is successfully deleted!");
    }

    public CommonResult getOrganizationTransactionType(Long orgId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(organization.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_GET);
        }
        List<TransactionTypeDto> transactionTypeDtoList = transactionTypeRepository.findAllByOrgIdAndIsDeletedAndIsPersonal(organization.getId(), Boolean.FALSE, Boolean.FALSE)
                .stream().map(transactionType -> new TransactionTypeDto(
                        transactionType.getId(),
                        transactionType.getName()
                )).toList();
        return new CommonResult(transactionTypeDtoList);
    }

    public CommonResult createOrganizationTransaction(TranCreateDto tranCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(tranCreateDto.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(tranCreateDto.getTransactionTypeId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_add_transaction);

        Transaction transaction = new Transaction();
        transaction.setTypeId(transactionType.getId());
        transaction.setIsDeleted(Boolean.FALSE);
        transaction.setIsPersonal(Boolean.FALSE);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAmount(tranCreateDto.getAmount());
        transaction.setOrgId(organization.getId());
        transaction.setIsIncome(tranCreateDto.getIsIncome());
        transaction.setDescription(tranCreateDto.getDescription());
        Transaction savedFO = transactionRepository.save(transaction);
        return new CommonResult(savedFO.getId() + " is successfully saved");
    }

    private Worker getWorker(Long currentUserId, Long orgId) {
        return workerRepository.findByUserIdAndOrgIdAndIsDeleted(currentUserId, orgId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.CANNOT_CREATE));
    }

    public CommonResult editOrganizationTransaction(TransactionUpdateDto transactionUpdateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndIsDeletedAndIsPersonal(transactionUpdateDto.getId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transaction.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_edit_transaction);


        if (Objects.equals(transaction.getAmount(), transactionUpdateDto.getAmount())) {
            transaction.setAmount(transactionUpdateDto.getAmount());
        }
        if (Objects.equals(transaction.getDescription(), transactionUpdateDto.getDescription())) {
            transaction.setDescription(transactionUpdateDto.getDescription());
        }
        if (Objects.equals(transaction.getTypeId(), transactionUpdateDto.getTransactionTypeId())) {
            TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(transactionUpdateDto.getTransactionTypeId(), Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            if (!Objects.equals(transaction.getOrgId(), transactionType.getOrgId())) {
                throw new GenericException(Errors.CANNOT_UPDATE);
            }
            transaction.setTypeId(transactionUpdateDto.getTransactionTypeId());
        }
        if (Objects.equals(transaction.getIsIncome(), transactionUpdateDto.getIsIncome())) {
            transaction.setIsIncome(transactionUpdateDto.getIsIncome());
        }


        Transaction savedTransaction = transactionRepository.save(transaction);
        return new CommonResult(savedTransaction.getId() + " is successfully updated");
    }


    public CommonResult deleteOrganizationTransaction(Long TransactionId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndIsDeletedAndIsPersonal(TransactionId, Boolean.FALSE, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Organization organization = organizationRepository.findByIdAndIsDeleted(transaction.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_delete_transaction);
        transaction.setIsDeleted(Boolean.TRUE);
        Transaction deletedTransaction = transactionRepository.save(transaction);
        return new CommonResult(deletedTransaction.getId() + " is successfully deleted!");
    }

    public CommonResult getOrganizationTransactionList(int size, int page, OrganizationTranCriteria organizationTranCriteria) {
        User currentUser = SecurityUtils.getCurrentUser();
        Organization organization = organizationRepository.findByIdAndIsDeleted(organizationTranCriteria.getOrgId(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        checkWorkerPermission(currentUser, organization, WorkerPermissionConstants.can_delete_transaction);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setIncome(new ArrayList<>());
        transactionResponse.setOutcome(new ArrayList<>());

        List<Transaction> transactionList = transactionRepository.getOrganizationTransactionsList(
                organizationTranCriteria.getSortedByAmount(),
                organizationTranCriteria.getMinAmount(),
                organizationTranCriteria.getMaxAmount(),
                organizationTranCriteria.getSortedByTransactionType(),
                organizationTranCriteria.getTransactionTypeId(),
                organizationTranCriteria.getOrgId(),
                size,
                page
        );
        for (Transaction transaction : transactionList) {
            if (organizationTranCriteria.getSortedByDate()) {
                if (
                        transaction.getCreatedAt().isBefore(organizationTranCriteria.getFromDate()) &&
                                transaction.getCreatedAt().isAfter(organizationTranCriteria.getToDate())
                ) {
                    continue;
                }
            }
            if (transaction.getIsIncome()) {
                transactionResponse.getIncome().add(transaction);
            } else {
                transactionResponse.getOutcome().add(transaction);
            }
        }
        return new CommonResult(transactionResponse, Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
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
                throw new GenericException(Errors.CANNOT_CREATE);
            }
        }
    }
}
