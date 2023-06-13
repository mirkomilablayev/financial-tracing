package com.finanacialtracing.financialtracingapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "worker_positions")
public class WorkerPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long orgId;
    private Boolean isDeleted = false;
}
