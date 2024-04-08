package com.voltmoney.scheduler.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "service_operators")
@Data
public class ServiceOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "status", nullable = false)
    private Boolean status;

}