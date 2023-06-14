package com.finanacialtracing.controller;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.AddAdminDto;
import com.finanacialtracing.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/add-admin")
    public CommonResult addAdmin(AddAdminDto addAdminDto){
        log.info("rest request to addAdmin()");
        return adminService.addAdmin(addAdminDto);
    }

    @DeleteMapping("/delete-admin")
    public CommonResult deleteAdmin(Long adminId){
        log.info("rest request to deleteAdmin()");
        return adminService.deleteAdmin(adminId);
    }

}
