package com.finanacialtracing.dto.transactiontype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalTransactionTypeDTO {
    @NonNull
    private String name;
}
