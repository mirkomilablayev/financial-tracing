package com.finanacialtracing.dto.financialoperation;

import com.finanacialtracing.entity.FinancialOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FinancialOperationResponse {
    private List<FinancialOperation> income;
    private List<FinancialOperation> outcome;
}
