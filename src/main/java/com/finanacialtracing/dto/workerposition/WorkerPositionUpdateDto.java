package com.finanacialtracing.dto.workerposition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPositionUpdateDto {
    private Long workerPositionId;
    private String newPositionName;
}
