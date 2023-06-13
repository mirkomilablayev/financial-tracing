package com.finanacialtracing.financialtracingapp.dto.financialoperation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPersonalFinancialOperationDTO {
    private Boolean sortedByAmount = false;
    private Double minAmount;
    private Double maxAmount;

    private Boolean sortedByDate = false;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private Boolean sortedByFoType = false;
    private Long foTypeId;
}
