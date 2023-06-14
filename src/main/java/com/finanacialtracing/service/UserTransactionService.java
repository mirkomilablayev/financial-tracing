package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCriteria;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCreateDto;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeDTO;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeUpdateDTO;
import com.finanacialtracing.exception.GenericException;

public interface UserTransactionService {
    CommonResult createTransactionType(PersonalTransactionTypeDTO personalTransactionTypeDTO) throws GenericException;
    CommonResult updateTransactionType(PersonalTransactionTypeUpdateDTO personalTransactionTypeUpdateDTO);
    CommonResult deleteTransactionType(Long transactionTypeId);
    CommonResult getPersonalTransactionTypes();

    CommonResult createTransaction(PersonalTranCreateDto personalTranCreateDto);
    CommonResult updateTransaction(TransactionUpdateDto transactionUpdateDto);
    CommonResult deletedTransaction(Long foId);
    CommonResult getTransactionList(int size, int page, PersonalTranCriteria personalTranCriteria);

}
