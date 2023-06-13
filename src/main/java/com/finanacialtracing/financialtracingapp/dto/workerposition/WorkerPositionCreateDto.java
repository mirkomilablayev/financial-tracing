package com.finanacialtracing.financialtracingapp.dto.workerposition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPositionCreateDto {
    private Long orgId;
    private String positionName;
}
