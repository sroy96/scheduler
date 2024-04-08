package com.voltmoney.scheduler.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    private Long appointmentId;
    @Column(name = "slot")
    private String slot; // "0-1"
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "operator_id")
    private Long operatorId;


}