package com.finanacialtracing.financialtracingapp.dto.financialoperation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialOperationDTO {
    private Long id;
    private Double amount;
}
