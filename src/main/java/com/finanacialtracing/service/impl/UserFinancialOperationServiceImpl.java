package com.finanacialtracing.service.impl;


import com.finanacialtracing.dto.transaction.FinancialOperationResponse;
import com.finanacialtracing.entity.TransactionType;
import com.finanacialtracing.entity.Transaction;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.personal.PersonalFinancialOperationCriteria;
import com.finanacialtracing.dto.transaction.personal.PersonalFinancialOperationCreateDto;
import com.finanacialtracing.dto.transaction.FinancialOperationUpdateDto;
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
        TransactionType transactionType = new TransactionType();
        transactionType.setName(personalFoCreateTypeDTO.getName());
        transactionType.setIsPersonal(Boolean.TRUE);
        transactionType.setUserId(currentUser.getId());
        TransactionType savedTransactionType = foTypeRepository.save(transactionType);
        return new CommonResult(savedTransactionType);
    }

    @Override
    public CommonResult updateFOType(PersonalFOUpdateTypeDTO personalFOUpdateTypeDTO) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(personalFOUpdateTypeDTO.getFoTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(transactionType.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE_FO_TYPE);
        }
        transactionType.setName(personalFOUpdateTypeDTO.getNewFoTypeName());
        TransactionType updatedTransactionType = foTypeRepository.save(transactionType);
        return new CommonResult(" Financial Operation Type is updated to " + updatedTransactionType.getName());
    }

    @Override
    public CommonResult deleteFOType(Long foTypeId) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(foTypeId, Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(transactionType.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE_FO_TYPE);
        }
        if (!isUsedBefore(transactionType, currentUser)) {
            throw new GenericException(Errors.CANNOT_DELETE_FO_TYPE);
        }
        transactionType.setIsDeleted(Boolean.TRUE);
        TransactionType deletedTransactionType = foTypeRepository.save(transactionType);
        return new CommonResult(deletedTransactionType.getName() + " is Successfully deleted");
    }

    private Boolean isUsedBefore(TransactionType transactionType, User currentUser) {
        List<Transaction> transactionList = financialOperationRepository.getFinancialOperations(currentUser.getId(), transactionType.getId(), Boolean.FALSE, Boolean.TRUE);
        return transactionList.size() == 0;
    }

    @Override
    public CommonResult getPersonalFoTypes() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<TransactionType> transactionTypeList = foTypeRepository.findAllByUserIdAndIsDeletedAndIsPersonal(currentUser.getId(), Boolean.FALSE, Boolean.TRUE);
        return new CommonResult(transactionTypeList);
    }

    @Override
    public CommonResult createFO(PersonalFinancialOperationCreateDto personalFinancialOperationCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(personalFinancialOperationCreateDto.getFoTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Transaction transaction = new Transaction();
        transaction.setUserId(currentUser.getId());
        transaction.setIsPersonal(Boolean.TRUE);
        transaction.setTypeId(transactionType.getId());
        transaction.setAmount(personalFinancialOperationCreateDto.getAmount());
        transaction.setDescription(personalFinancialOperationCreateDto.getDescription());
        transaction.setIsIncome(personalFinancialOperationCreateDto.getIsIncome());
        transaction.setCreatedAt(LocalDateTime.now());
        Transaction savedTransaction = financialOperationRepository.save(transaction);
        return new CommonResult(savedTransaction, Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

    @Override
    public CommonResult updateFO(FinancialOperationUpdateDto financialOperationUpdateDto) {
        Transaction transaction = financialOperationRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationUpdateDto.getId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        if (!Objects.equals(transaction.getAmount(), financialOperationUpdateDto.getAmount())) {
            transaction.setAmount(financialOperationUpdateDto.getAmount());
        }

        if (!Objects.equals(transaction.getDescription(), financialOperationUpdateDto.getDescription())) {
            transaction.setDescription(financialOperationUpdateDto.getDescription());
        }

        if (!Objects.equals(transaction.getIsIncome(), financialOperationUpdateDto.getIsIncome())) {
            transaction.setIsIncome(financialOperationUpdateDto.getIsIncome());
        }

        if (!Objects.equals(transaction.getTypeId(), financialOperationUpdateDto.getFoTypeId())) {
            TransactionType transactionType = foTypeRepository.findByIdAndIsDeletedAndIsPersonal(financialOperationUpdateDto.getFoTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            transaction.setTypeId(transactionType.getId());
        }

        Transaction updatedTransaction = financialOperationRepository.save(transaction);
        return new CommonResult(updatedTransaction.getId() + " is Successfully Updated", Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

    @Override
    public CommonResult deletedFO(Long foId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = financialOperationRepository.findByIdAndIsDeletedAndIsPersonal(foId, Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Long userId = transaction.getUserId();
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new GenericException(Errors.CANNOT_DISABLE_FO);
        }
        transaction.setIsDeleted(Boolean.TRUE);
        Transaction deletedFinancialOPeration = financialOperationRepository.save(transaction);
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

        List<Transaction> financialOperationsList = financialOperationRepository.getPersonalFinancialOperationsList(
                personalFinancialOperationCriteria.getSortedByAmount(),
                personalFinancialOperationCriteria.getMinAmount(),
                personalFinancialOperationCriteria.getMaxAmount(),
                personalFinancialOperationCriteria.getSortedByFoType(),
                personalFinancialOperationCriteria.getFoTypeId(),
                currentUser.getId(),
                size,
                page
        );
        for (Transaction transaction : financialOperationsList) {
            if (personalFinancialOperationCriteria.getSortedByDate()) {
                if (
                        transaction.getCreatedAt().isBefore(personalFinancialOperationCriteria.getFromDate()) &&
                                transaction.getCreatedAt().isAfter(personalFinancialOperationCriteria.getToDate())
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

}
