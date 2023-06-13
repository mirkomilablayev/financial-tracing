package com.finanacialtracing.financialtracingapp.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUpdateDto {
    private Long orgId;
    private String orgName;
}
