package com.finanacialtracing.dto.transaction;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FinancialOperationUpdateDto {
    @NonNull
    private Long id;
    private Double amount;
    private String description;
    private Boolean isIncome;
    private Long foTypeId;
}