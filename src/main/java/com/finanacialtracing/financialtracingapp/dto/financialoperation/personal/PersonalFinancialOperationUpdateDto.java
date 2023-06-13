package com.finanacialtracing.financialtracingapp.dto.financialoperation.personal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonalFinancialOperationUpdateDto {
    @NonNull
    private Long id;
    private Double amount;
    private String description;
    private Boolean isIncome;
    private Long foTypeId;
}
