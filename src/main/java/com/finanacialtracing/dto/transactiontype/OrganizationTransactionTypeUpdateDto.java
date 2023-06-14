package com.finanacialtracing.dto.transactiontype;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationTransactionTypeUpdateDto {
    private Long transactionTypeId;
    private String newName;
}
