package com.finanacialtracing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "financial_operations")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String description;
    private Boolean isIncome;
    private Boolean isDeleted = false;
    private Boolean isPersonal = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    private Long userId;
    private Long orgId;
    private Long typeId;
}
