package com.finanacialtracing.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkerDto {
    private Long workerId;
    private Long newPositionId;
    private List<Long> newPermissions;
}
