package com.finanacialtracing.dto.transaction.organization;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TranCreateDto {
    private Long orgId;
    @NonNull
    private Double amount;
    private String description;
    @NonNull
    private Boolean isIncome;
    @NonNull
    private Long foTypeId;
}
