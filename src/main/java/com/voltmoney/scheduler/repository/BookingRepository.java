package com.voltmoney.scheduler.repository;

import com.voltmoney.scheduler.dao.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByOperatorIdAndDate(Long operatorId, LocalDate date);

    List<Appointment> findByOperatorId(Long operatorId);

    List<Appointment> findByOperatorIdAndDateAndSlot(Long operatorId, LocalDate date, String slot);
}
