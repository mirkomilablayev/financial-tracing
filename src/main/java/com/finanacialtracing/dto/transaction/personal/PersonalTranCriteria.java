package com.finanacialtracing.dto.transaction.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalTranCriteria {
    private Boolean sortedByAmount = false;
    private Double minAmount;
    private Double maxAmount;

    private Boolean sortedByDate = false;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private Boolean sortedByTransactionType = false;
    private Long transactionTypeId;
}
