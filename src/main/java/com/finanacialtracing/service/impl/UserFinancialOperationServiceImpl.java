package com.finanacialtracing.service.impl;


import com.finanacialtracing.dto.financialoperation.FinancialOperationResponse;
import com.finanacialtracing.entity.FOType;
import com.finanacialtracing.entity.FinancialOperation;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCriteria;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCreateDto;
import com.finanacialtracing.dto.financialoperation.FinancialOperationUpdateDto;
import com.finanacialtracing.dto.foType.PersonalFOCreateTypeDTO;
import com.finanacialtracing.dto.foType.PersonalFOUpdateTypeDTO;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.repository.FOTypeRepository;
import com.finanacialtracing.repository.FinancialOperationRepository;
import com.finanacialtracing.service.UserFinancialOperationService;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserFinancialOperationServiceImpl implements UserFinancialOperationService {

    private final FOTypeRepository foTypeRepository;
    private final FinancialOperationRepository financialOperationRepository;

    @Override
    public CommonResult createFOType(PersonalFOCreateTypeDTO personalFoCreateTypeDTO) throws GenericException {
        User currentUser = SecurityUtils.getCurrentUser();
        FOType foType = new FOType();
        foType.setName(personalFoCreateTypeDTO.getName());
        foType.setIsPersonal(Boolean.TRUE);
        foType.setUserId(currentUser.getId());
        FOType savedFOType = foTypeRepository.save(foType);
        return new CommonResult(savedFOType);
    }

    @Override
    public CommonResult updateFOType(PersonalFOUpdateTypeDTO personalFOUpdateTypeDTO) {
        User currentUser = SecurityUtils.getCurrentUser();
        FOType foType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(personalFOUpdateTypeDTO.getFoTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(foType.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE_FO_TYPE);
        }
        foType.setName(personalFOUpdateTypeDTO.getNewFoTypeName());
        FOType updatedFoType = foTypeRepository.save(foType);
        return new CommonResult(" Financial Operation Type is updated to " + updatedFoType.getName());
    }

    @Override
    public CommonResult deleteFOType(Long foTypeId) {
        User currentUser = SecurityUtils.getCurrentUser();
        FOType foType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(foTypeId, Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(foType.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE_FO_TYPE);
        }
        if (!isUsedBefore(foType, currentUser)) {
            throw new GenericException(Errors.CANNOT_DELETE_FO_TYPE);
        }
        foType.setIsDeleted(Boolean.TRUE);
        FOType deletedFoType = foTypeRepository.save(foType);
        return new CommonResult(deletedFoType.getName() + " is Successfully deleted");
    }

    private Boolean isUsedBefore(FOType foType, User currentUser) {
        List<FinancialOperation> financialOperationList = financialOperationRepository.getFinancialOperations(currentUser.getId(), foType.getId(), Boolean.FALSE, Boolean.TRUE);
        return financialOperationList.size() == 0;
    }

    @Override
    public CommonResult getPersonalFoTypes() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<FOType> foTypeList = foTypeRepository.findAllByUserIdAndIsDeletedAndIsPersonal(currentUser.getId(), Boolean.FALSE, Boolean.TRUE);
        return new CommonResult(foTypeList);
    }

    @Override
    public CommonResult createFO(PersonalFinancialOperationCreateDto personalFinancialOperationCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        FOType foType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(personalFinancialOperationCreateDto.getFoTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        FinancialOperation financialOperation = new FinancialOperation();
        financialOperation.setUserId(currentUser.getId());
        financialOperation.setIsPersonal(Boolean.TRUE);
        financialOperation.setTypeId(foType.getId());
        financialOperation.setAmount(personalFinancialOperationCreateDto.getAmount());
        financialOperation.setDescription(personalFinancialOperationCreateDto.getDescription());
        financialOperation.setIsIncome(personalFinancialOperationCreateDto.getIsIncome());
        financialOperation.setCreatedAt(LocalDateTime.now());
        FinancialOperation savedFinancialOperation = financialOperationRepository.save(financialOperation);
        return new CommonResult(savedFinancialOperation, Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

    @Override
    public CommonResult updateFO(FinancialOperationUpdateDto financialOperationUpdateDto) {
        FinancialOperation financialOperation = financialOperationRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationUpdateDto.getId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        if (!Objects.equals(financialOperation.getAmount(), financialOperationUpdateDto.getAmount())) {
            financialOperation.setAmount(financialOperationUpdateDto.getAmount());
        }

        if (!Objects.equals(financialOperation.getDescription(), financialOperationUpdateDto.getDescription())) {
            financialOperation.setDescription(financialOperationUpdateDto.getDescription());
        }

        if (!Objects.equals(financialOperation.getIsIncome(), financialOperationUpdateDto.getIsIncome())) {
            financialOperation.setIsIncome(financialOperationUpdateDto.getIsIncome());
        }

        if (!Objects.equals(financialOperation.getTypeId(), financialOperationUpdateDto.getFoTypeId())) {
            FOType foType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationUpdateDto.getFoTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            financialOperation.setTypeId(foType.getId());
        }

        FinancialOperation updatedFinancialOperation = financialOperationRepository.save(financialOperation);
        return new CommonResult(updatedFinancialOperation.getId() + " is Successfully Updated", Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

    @Override
    public CommonResult deletedFO(Long foId) {
        User currentUser = SecurityUtils.getCurrentUser();
        FinancialOperation financialOperation = financialOperationRepository.findByIdAndIsDeletedAndIsPersonal(foId, Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Long userId = financialOperation.getUserId();
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new GenericException(Errors.CANNOT_DISABLE_FO);
        }
        financialOperation.setIsDeleted(Boolean.TRUE);
        FinancialOperation deletedFinancialOPeration = financialOperationRepository.save(financialOperation);
        return new CommonResult(deletedFinancialOPeration.getId() + " is Successfully deleted");
    }

    @Override
    public CommonResult getFoList(int size,
                                  int page,
                                  PersonalFinancialOperationCriteria personalFinancialOperationCriteria) {
        User currentUser = SecurityUtils.getCurrentUser();
        FinancialOperationResponse financialOperationResponse = new FinancialOperationResponse();
        financialOperationResponse.setIncome(new ArrayList<>());
        financialOperationResponse.setOutcome(new ArrayList<>());

        List<FinancialOperation> financialOperationsList = financialOperationRepository.getPersonalFinancialOperationsList(
                personalFinancialOperationCriteria.getSortedByAmount(),
                personalFinancialOperationCriteria.getMinAmount(),
                personalFinancialOperationCriteria.getMaxAmount(),
                personalFinancialOperationCriteria.getSortedByFoType(),
                personalFinancialOperationCriteria.getFoTypeId(),
                currentUser.getId(),
                size,
                page
        );
        for (FinancialOperation financialOperation : financialOperationsList) {
            if (personalFinancialOperationCriteria.getSortedByDate()) {
                if (
                        financialOperation.getCreatedAt().isBefore(personalFinancialOperationCriteria.getFromDate()) &&
                                financialOperation.getCreatedAt().isAfter(personalFinancialOperationCriteria.getToDate())
                ) {
                    continue;
                }
            }
            if (financialOperation.getIsIncome()) {
                financialOperationResponse.getIncome().add(financialOperation);
            } else {
                financialOperationResponse.getOutcome().add(financialOperation);
            }
        }
        return new CommonResult(financialOperationResponse, Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

}
