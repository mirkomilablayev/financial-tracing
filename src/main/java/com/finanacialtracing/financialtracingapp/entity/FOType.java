package com.finanacialtracing.financialtracingapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "fo_types")
public class FOType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean isPersonal;
    private Boolean isDeleted = false;
    private Long userId;
    private Long orgId;
}
