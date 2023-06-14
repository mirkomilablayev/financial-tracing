package com.finanacialtracing.controller;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCriteria;
import com.finanacialtracing.dto.financialoperation.personal.PersonalFinancialOperationCreateDto;
import com.finanacialtracing.dto.financialoperation.FinancialOperationUpdateDto;
import com.finanacialtracing.dto.foType.PersonalFOCreateTypeDTO;
import com.finanacialtracing.dto.foType.PersonalFOUpdateTypeDTO;
import com.finanacialtracing.service.UserFinancialOperationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-fo")
@AllArgsConstructor
@Slf4j
public class UserFinancialOperationController {

    private final UserFinancialOperationService foService;


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
    public CommonResult updatePersonalFinancialOperation(@RequestBody FinancialOperationUpdateDto financialOperationUpdateDto) {
        log.info("Rest request to updatePersonalFinancialOperation() with param={}", financialOperationUpdateDto);
        return foService.updateFO(financialOperationUpdateDto);
    }

    @DeleteMapping("/delete-pfo/{foId}")
    public CommonResult deletePersonalFinancialOperation(@PathVariable Long foId) {
        log.info("Rest request deletePersonalFinancialOperation() with param={}", foId);
        return foService.deletedFO(foId);
    }

    @GetMapping("/get-ufo-list")
    CommonResult getFoList(@RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestBody PersonalFinancialOperationCriteria personalFinancialOperationCriteria) {
        return foService.getFoList(size, page, personalFinancialOperationCriteria);
    }


}
