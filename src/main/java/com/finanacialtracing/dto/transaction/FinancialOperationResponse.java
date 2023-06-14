package com.finanacialtracing.dto.transaction;

import com.finanacialtracing.entity.Transaction;
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
    private List<Transaction> income;
    private List<Transaction> outcome;
}
