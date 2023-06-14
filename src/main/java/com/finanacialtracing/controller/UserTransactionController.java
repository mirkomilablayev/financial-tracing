package com.finanacialtracing.controller;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCriteria;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCreateDto;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeDTO;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeUpdateDTO;
import com.finanacialtracing.service.UserTransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-tran")
@AllArgsConstructor
@Slf4j
public class UserTransactionController {

    private final UserTransactionService tranService;


    @PostMapping("/create-p-tran-type")
    public CommonResult createTransactionType(@RequestBody PersonalTransactionTypeDTO personalTransactionTypeDTO) {
        log.info("rest request to createTransactionType() with param={}", personalTransactionTypeDTO);
        return tranService.createTransactionType(personalTransactionTypeDTO);
    }

    @PostMapping("/update-p-tran-type")
    public CommonResult updateTransactionType(@RequestBody PersonalTransactionTypeUpdateDTO personalTransactionTypeUpdateDTO) {
        log.info("rest request to updateTransactionType() with param={}", personalTransactionTypeUpdateDTO);
        return tranService.updateTransactionType(personalTransactionTypeUpdateDTO);
    }

    @DeleteMapping("/delete-p-tran-type/{transactionTypeId}")
    public CommonResult deletePTransactionType(@PathVariable Long transactionTypeId) {
        log.info("rest request to deletePTransactionType() with param={}", transactionTypeId);
        return tranService.deleteTransactionType(transactionTypeId);
    }

    @GetMapping("/get-p-tran-types")
    public CommonResult getPersonalTransactionTypes() {
        log.info("rest request to getPersonalTransactionTypes()");
        return tranService.getPersonalTransactionTypes();
    }

    @PostMapping("/create-p-tran")
    public CommonResult createPersonalTransaction(@RequestBody PersonalTranCreateDto personalTranCreateDto) {
        log.info("Rest request to createPersonalTransaction() with param={}", personalTranCreateDto);
        return tranService.createTransaction(personalTranCreateDto);
    }

    @PutMapping("/update-p-tran")
    public CommonResult updatePersonalTransaction(@RequestBody TransactionUpdateDto transactionUpdateDto) {
        log.info("Rest request to updatePersonalTransaction() with param={}", transactionUpdateDto);
        return tranService.updateTransaction(transactionUpdateDto);
    }

    @DeleteMapping("/delete-p-tran/{tranId}")
    public CommonResult deletePersonalTransaction(@PathVariable Long tranId) {
        log.info("Rest request deletePersonalTransaction() with param={}", tranId);
        return tranService.deletedTransaction(tranId);
    }

    @GetMapping("/get-p-tran-list")
    CommonResult getTransactionList(@RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestBody PersonalTranCriteria personalTranCriteria) {
        return tranService.getTransactionList(size, page, personalTranCriteria);
    }


}
