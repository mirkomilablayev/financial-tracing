package com.finanacialtracing.financialtracingapp.controller;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;
import com.finanacialtracing.financialtracingapp.dto.financialoperation.personal.GetPersonalFinancialOperationDTO;
import com.finanacialtracing.financialtracingapp.dto.financialoperation.personal.PersonalFinancialOperationCreateDto;
import com.finanacialtracing.financialtracingapp.dto.financialoperation.personal.PersonalFinancialOperationUpdateDto;
import com.finanacialtracing.financialtracingapp.dto.foType.PersonalFOCreateTypeDTO;
import com.finanacialtracing.financialtracingapp.dto.foType.PersonalFOUpdateTypeDTO;
import com.finanacialtracing.financialtracingapp.service.UserFoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-fo")
@AllArgsConstructor
@Slf4j
public class UserFOController {

    private final UserFoService foService;


    @PostMapping("/create-fo-type")
    public CommonResult createFOType(@RequestBody PersonalFOCreateTypeDTO personalFoCreateTypeDTO) {
        log.info("rest request to createFOType() with param={}", personalFoCreateTypeDTO);
        return foService.createFOType(personalFoCreateTypeDTO);
    }

    @PostMapping("/update-pfo-type")
    public CommonResult updateFoType(@RequestBody PersonalFOUpdateTypeDTO personalFOUpdateTypeDTO) {
        log.info("rest request to updateFoType() with param={}", personalFOUpdateTypeDTO);
        return foService.updateFOType(personalFOUpdateTypeDTO);
    }

    @DeleteMapping("/delete-pfo-type/{foTypeId}")
    public CommonResult deletePFOType(@PathVariable Long foTypeId) {
        log.info("rest request to deletePFOType() with param={}", foTypeId);
        return foService.deleteFOType(foTypeId);
    }

    @GetMapping("/get-pfo-types")
    public CommonResult getPersonalFoTypes() {
        log.info("rest request to getPersonalFoTypes()");
        return foService.getPersonalFoTypes();
    }

    @PostMapping("/create-pfo")
    public CommonResult createPersonalFinancialOperation(@RequestBody PersonalFinancialOperationCreateDto personalFinancialOperationCreateDto) {
        log.info("Rest request to createPersonalFinancialOperation() with param={}", personalFinancialOperationCreateDto);
        return foService.createFO(personalFinancialOperationCreateDto);
    }

    @PutMapping("/update-pfo")
    public CommonResult updatePersonalFinancialOperation(@RequestBody PersonalFinancialOperationUpdateDto personalFinancialOperationUpdateDto) {
        log.info("Rest request to updatePersonalFinancialOperation() with param={}", personalFinancialOperationUpdateDto);
        return foService.updateFO(personalFinancialOperationUpdateDto);
    }

    @DeleteMapping("/delete-pfo/{foId}")
    public CommonResult deletePersonalFinancialOperation(@PathVariable Long foId) {
        log.info("Rest request deletePersonalFinancialOperation() with param={}", foId);
        return foService.deletedFO(foId);
    }

    @GetMapping("/get-ufo-list")
    CommonResult getFoList(@RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestBody GetPersonalFinancialOperationDTO getPersonalFinancialOperationDTO) {
        return foService.getFoList(size, page, getPersonalFinancialOperationDTO);
    }


}
