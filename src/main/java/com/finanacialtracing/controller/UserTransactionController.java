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


    /**
     * create personal transaction type
     */
    @PostMapping("/create-personal-tran-type")
    public CommonResult createTransactionType(@RequestBody PersonalTransactionTypeDTO personalTransactionTypeDTO) {
        log.info("rest request to createTransactionType() with param={}", personalTransactionTypeDTO);
        return tranService.createTransactionType(personalTransactionTypeDTO);
    }

    /**
     * update personal transaction type
     */
    @PostMapping("/update-personal-tran-type")
    public CommonResult updateTransactionType(@RequestBody PersonalTransactionTypeUpdateDTO personalTransactionTypeUpdateDTO) {
        log.info("rest request to updateTransactionType() with param={}", personalTransactionTypeUpdateDTO);
        return tranService.updateTransactionType(personalTransactionTypeUpdateDTO);
    }

    /**
     * delete personal transaction type by id
     */

    @DeleteMapping("/delete-personal-tran-type/{transactionTypeId}")
    public CommonResult deletePTransactionType(@PathVariable Long transactionTypeId) {
        log.info("rest request to deletePTransactionType() with param={}", transactionTypeId);
        return tranService.deleteTransactionType(transactionTypeId);
    }

    /**
     * get personal transaction type list
     */
    @GetMapping("/get-personal-tran-types")
    public CommonResult getPersonalTransactionTypes() {
        log.info("rest request to getPersonalTransactionTypes()");
        return tranService.getPersonalTransactionTypes();
    }


    /**
     * create personal transaction
     */
    @PostMapping("/create-personal-tran")
    public CommonResult createPersonalTransaction(@RequestBody PersonalTranCreateDto personalTranCreateDto) {
        log.info("Rest request to createPersonalTransaction() with param={}", personalTranCreateDto);
        return tranService.createTransaction(personalTranCreateDto);
    }

    /**
     * update personal transaction
     */
    @PutMapping("/update-personal-tran")
    public CommonResult updatePersonalTransaction(@RequestBody TransactionUpdateDto transactionUpdateDto) {
        log.info("Rest request to updatePersonalTransaction() with param={}", transactionUpdateDto);
        return tranService.updateTransaction(transactionUpdateDto);
    }

    /**
     * delete personal transaction by id
     */
    @DeleteMapping("/delete-personal-tran/{tranId}")
    public CommonResult deletePersonalTransaction(@PathVariable Long tranId) {
        log.info("Rest request deletePersonalTransaction() with param={}", tranId);
        return tranService.deletedTransaction(tranId);
    }

    /**
     * get personal transaction list
     * you can filter response here
     */
    @GetMapping("/get-personal-tran-list")
    CommonResult getTransactionList(@RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestBody PersonalTranCriteria personalTranCriteria) {
        log.info("Rest request to getTransactionList()");
        return tranService.getTransactionList(size, page, personalTranCriteria);
    }


}
