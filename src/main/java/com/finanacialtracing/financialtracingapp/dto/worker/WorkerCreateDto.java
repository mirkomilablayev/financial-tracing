package com.finanacialtracing.financialtracingapp.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerCreateDto {
    private Long userId;
    private Long orgId;
    private Long positionId;
    private List<Long> permissionIds;
}
