package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.AddAdminDto;

public interface AdminService {
    CommonResult addAdmin(AddAdminDto addAdminDto);
    CommonResult deleteAdmin(Long adminId);

}
