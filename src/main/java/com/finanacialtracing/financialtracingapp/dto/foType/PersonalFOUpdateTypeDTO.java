package com.finanacialtracing.financialtracingapp.dto.foType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalFOUpdateTypeDTO {
    private Long foTypeId;
    private String newFoTypeName;
}
