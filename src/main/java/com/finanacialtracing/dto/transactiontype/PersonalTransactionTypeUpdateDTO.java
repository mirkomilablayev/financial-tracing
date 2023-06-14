package com.finanacialtracing.dto.transactiontype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalTransactionTypeUpdateDTO {
    private Long transactionTypeId;
    private String newTransactionTypeName;
}
