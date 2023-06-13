package com.finanacialtracing.financialtracingapp.service;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;
import com.finanacialtracing.financialtracingapp.dto.financialoperation.GetPersonalFinancialOperationDTO;
import com.finanacialtracing.financialtracingapp.dto.financialoperation.PersonalFinancialOperationCreateDto;
import com.finanacialtracing.financialtracingapp.dto.financialoperation.PersonalFinancialOperationUpdateDto;
import com.finanacialtracing.financialtracingapp.dto.foType.PersonalFOCreateTypeDTO;
import com.finanacialtracing.financialtracingapp.dto.foType.PersonalFOUpdateTypeDTO;
import com.finanacialtracing.financialtracingapp.exception.GenericException;

public interface UserFoService {
    CommonResult createFOType(PersonalFOCreateTypeDTO personalFoCreateTypeDTO) throws GenericException;
    CommonResult updateFOType(PersonalFOUpdateTypeDTO personalFOUpdateTypeDTO);
    CommonResult deleteFOType(Long foTypeId);
    CommonResult getPersonalFoTypes();

    CommonResult createFO(PersonalFinancialOperationCreateDto personalFinancialOperationCreateDto);
    CommonResult updateFO(PersonalFinancialOperationUpdateDto personalFinancialOperationUpdateDto);
    CommonResult deletedFO(Long foId);
    CommonResult getFoList(int size, int page, GetPersonalFinancialOperationDTO getPersonalFinancialOperationDTO);

}
