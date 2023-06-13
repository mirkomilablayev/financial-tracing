package com.finanacialtracing.financialtracingapp.service;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;

public interface OrganizationFOService {
    CommonResult createOrganization();
    CommonResult updateOrganization();
    CommonResult deleteOrganization();
    CommonResult getOrganization();
    CommonResult getMyOrganizations();
    CommonResult getOrgWorkers();
    CommonResult getOrgFoTypes();
}
