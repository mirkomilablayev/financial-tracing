package com.finanacialtracing.dto.financialoperation.organization;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FinancialOperationCreateDto {
    private Long orgId;
    @NonNull
    private Double amount;
    private String description;
    @NonNull
    private Boolean isIncome;
    @NonNull
    private Long foTypeId;
}
