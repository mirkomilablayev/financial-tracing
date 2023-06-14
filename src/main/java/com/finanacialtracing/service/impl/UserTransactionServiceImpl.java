package com.finanacialtracing.service.impl;


import com.finanacialtracing.dto.transaction.TransactionResponse;
import com.finanacialtracing.entity.TransactionType;
import com.finanacialtracing.entity.Transaction;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCriteria;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCreateDto;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeDTO;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeUpdateDTO;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.repository.TransactionTypeRepository;
import com.finanacialtracing.repository.TransactionRepository;
import com.finanacialtracing.service.UserTransactionService;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserTransactionServiceImpl implements UserTransactionService {

    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public CommonResult createTransactionType(PersonalTransactionTypeDTO personalTransactionTypeDTO) throws GenericException {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = new TransactionType();
        transactionType.setName(personalTransactionTypeDTO.getName());
        transactionType.setIsPersonal(Boolean.TRUE);
        transactionType.setUserId(currentUser.getId());
        TransactionType savedTransactionType = transactionTypeRepository.save(transactionType);
        return new CommonResult(savedTransactionType);
    }

    @Override
    public CommonResult updateTransactionType(PersonalTransactionTypeUpdateDTO personalTransactionTypeUpdateDTO) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(personalTransactionTypeUpdateDTO.getTransactionTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(transactionType.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_UPDATE);
        }
        transactionType.setName(personalTransactionTypeUpdateDTO.getNewTransactionTypeName());
        TransactionType updatedTransactionType = transactionTypeRepository.save(transactionType);
        return new CommonResult(" Financial Operation Type is updated to " + updatedTransactionType.getName());
    }

    @Override
    public CommonResult deleteTransactionType(Long transactionTypeId) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(transactionTypeId, Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (!Objects.equals(transactionType.getUserId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        if (!isUsedBefore(transactionType, currentUser)) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        transactionType.setIsDeleted(Boolean.TRUE);
        TransactionType deletedTransactionType = transactionTypeRepository.save(transactionType);
        return new CommonResult(deletedTransactionType.getName() + " is Successfully deleted");
    }

    private Boolean isUsedBefore(TransactionType transactionType, User currentUser) {
        List<Transaction> transactionList = transactionRepository.getTransactions(currentUser.getId(), transactionType.getId(), Boolean.FALSE, Boolean.TRUE);
        return transactionList.size() == 0;
    }

    @Override
    public CommonResult getPersonalTransactionTypes() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAllByUserIdAndIsDeletedAndIsPersonal(currentUser.getId(), Boolean.FALSE, Boolean.TRUE);
        return new CommonResult(transactionTypeList);
    }

    @Override
    public CommonResult createTransaction(PersonalTranCreateDto personalTranCreateDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(personalTranCreateDto.getTransactionTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Transaction transaction = new Transaction();
        transaction.setUserId(currentUser.getId());
        transaction.setIsPersonal(Boolean.TRUE);
        transaction.setTypeId(transactionType.getId());
        transaction.setAmount(personalTranCreateDto.getAmount());
        transaction.setDescription(personalTranCreateDto.getDescription());
        transaction.setIsIncome(personalTranCreateDto.getIsIncome());
        transaction.setCreatedAt(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new CommonResult(savedTransaction, Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

    @Override
    public CommonResult updateTransaction(TransactionUpdateDto transactionUpdateDto) {
        Transaction transaction = transactionRepository.findByIdAndIsDeletedAndIsPersonal(transactionUpdateDto.getId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));

        if (!Objects.equals(transaction.getAmount(), transactionUpdateDto.getAmount())) {
            transaction.setAmount(transactionUpdateDto.getAmount());
        }

        if (!Objects.equals(transaction.getDescription(), transactionUpdateDto.getDescription())) {
            transaction.setDescription(transactionUpdateDto.getDescription());
        }

        if (!Objects.equals(transaction.getIsIncome(), transactionUpdateDto.getIsIncome())) {
            transaction.setIsIncome(transactionUpdateDto.getIsIncome());
        }

        if (!Objects.equals(transaction.getTypeId(), transactionUpdateDto.getTransactionTypeId())) {
            TransactionType transactionType = transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(transactionUpdateDto.getTransactionTypeId(), Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
            transaction.setTypeId(transactionType.getId());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return new CommonResult(updatedTransaction.getId() + " is Successfully Updated", Errors.SUCCESS.getCode(), Errors.SUCCESS.getMessage());
    }

    @Override
    public CommonResult deletedTransaction(Long foId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndIsDeletedAndIsPersonal(foId, Boolean.FALSE, Boolean.TRUE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        Long userId = transaction.getUserId();
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        transaction.setIsDeleted(Boolean.TRUE);
        Transaction deletedTrannsaction = transactionRepository.save(transaction);
        return new CommonResult(deletedTrannsaction.getId() + " is Successfully deleted");
    }

    @Override
    public CommonResult getTransactionList(int size,
                                           int page,
                                           PersonalTranCriteria personalTranCriteria) {
        User currentUser = SecurityUtils.getCurrentUser();
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setIncome(new ArrayList<>());
        transactionResponse.setOutcome(new ArrayList<>());

        List<Transaction> transactionList = transactionRepository.getPersonalTransactionsList(
                personalTranCriteria.getSortedByAmount(),
                personalTranCriteria.getMinAmount(),
                personalTranCriteria.getMaxAmount(),
                personalTranCriteria.getSortedByTransactionType(),
                personalTranCriteria.getTransactionTypeId(),
                currentUser.getId(),
                size,
                page
        );
        for (Transaction transaction : transactionList) {
            if (personalTranCriteria.getSortedByDate()) {
                if (
                        transaction.getCreatedAt().isBefore(personalTranCriteria.getFromDate()) &&
                                transaction.getCreatedAt().isAfter(personalTranCriteria.getToDate())
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

}
