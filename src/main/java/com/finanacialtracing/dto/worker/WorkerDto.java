package com.finanacialtracing.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDto {
    private Long id;
    private Long userId;
    private String username;
    private String userFullName;
    private Long positionId;
    private String positionName;
}
