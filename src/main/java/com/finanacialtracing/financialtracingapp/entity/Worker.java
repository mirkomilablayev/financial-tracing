package com.finanacialtracing.financialtracingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Organization organization;
    @ManyToOne
    private WorkerPosition position;
    @ManyToMany
    private Set<WorkerPermission> permissions;
    private Boolean isDeleted = false;
}
