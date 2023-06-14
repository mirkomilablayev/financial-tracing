package com.finanacialtracing.exception;

import com.finanacialtracing.dto.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerService extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleGenericException(GenericException ex) {
        log.error("Throwing exception, code: {}, message:{}", ex.getError().getCode(), ex.getError().getMessage());
        return ResponseEntity.ok(new CommonResult(null, ex.getError().getCode(), ex.getError().getMessage()));
    }


}
