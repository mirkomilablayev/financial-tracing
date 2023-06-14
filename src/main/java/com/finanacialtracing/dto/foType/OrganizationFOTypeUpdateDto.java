package com.finanacialtracing.dto.foType;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationFOTypeUpdateDto {
    private Long foTypeId;
    private String newName;
}
