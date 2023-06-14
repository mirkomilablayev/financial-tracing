package com.finanacialtracing.dto.foType;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationFOTypeCreateDto {
    private String name;
    private Long orgId;

}
