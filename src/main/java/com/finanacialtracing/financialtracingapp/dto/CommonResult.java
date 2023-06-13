package com.finanacialtracing.financialtracingapp.dto;

import com.finanacialtracing.financialtracingapp.exception.Errors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    private Object data;
    private int code;
    private String message;
    private String time = LocalDateTime.now().toString();

    public CommonResult(Object data){
        this.data = data;
        this.code = Errors.SUCCESS.getCode();
        this.message = Errors.SUCCESS.getMessage();
    }
    public CommonResult(Object data, Integer code, String message){
        this.data = data;
        this.code = code;
        this.message = message;
    }
    public CommonResult(Integer code, String message){
        this.code = Errors.SUCCESS.getCode();
        this.message = Errors.SUCCESS.getMessage();
    }

}
