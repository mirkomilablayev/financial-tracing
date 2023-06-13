package com.finanacialtracing.financialtracingapp.dto.foType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalFOCreateTypeDTO {
    @NonNull
    private String name;
}
