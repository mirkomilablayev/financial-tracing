package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.ChangePasswordDto;

public interface AuthorizationService {

    CommonResult changeUsername(String newUsername);
    CommonResult changePassword(ChangePasswordDto changePasswordDto);
    CommonResult deleteUser();
    CommonResult editFullName(String newFullName);


}
