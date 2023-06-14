package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCriteria;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCreateDto;
import com.finanacialtracing.dto.financialoperation.FinancialOperationUpdateDto;
import com.finanacialtracing.dto.foType.PersonalFOCreateTypeDTO;
import com.finanacialtracing.dto.foType.PersonalFOUpdateTypeDTO;
import com.finanacialtracing.exception.GenericException;

public interface UserFinancialOperationService {
    CommonResult createFOType(PersonalFOCreateTypeDTO personalFoCreateTypeDTO) throws GenericException;
    CommonResult updateFOType(PersonalFOUpdateTypeDTO personalFOUpdateTypeDTO);
    CommonResult deleteFOType(Long foTypeId);
    CommonResult getPersonalFoTypes();

    CommonResult createFO(PersonalFinancialOperationCreateDto personalFinancialOperationCreateDto);
    CommonResult updateFO(FinancialOperationUpdateDto financialOperationUpdateDto);
    CommonResult deletedFO(Long foId);
    CommonResult getFoList(int size, int page, PersonalFinancialOperationCriteria personalFinancialOperationCriteria);

}
