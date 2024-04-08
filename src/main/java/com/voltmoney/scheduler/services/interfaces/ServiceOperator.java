package com.voltmoney.scheduler.services.interfaces;

import com.voltmoney.scheduler.dto.response.OperatorAppointmentsResponse;

import java.time.LocalDate;

public interface ServiceOperator {

    OperatorAppointmentsResponse bookedSlots(Long operatorId, LocalDate date);
    OperatorAppointmentsResponse openSlots(Long operatorId,LocalDate date);

}
