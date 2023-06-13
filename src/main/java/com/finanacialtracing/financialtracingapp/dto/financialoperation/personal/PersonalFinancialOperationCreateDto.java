package com.finanacialtracing.financialtracingapp.dto.financialoperation.personal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonalFinancialOperationCreateDto {
    @NonNull
    private Double amount;
    private String description;
    @NonNull
    private Boolean isIncome;
    @NonNull
    private Long foTypeId;
}