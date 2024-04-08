package com.voltmoney.scheduler.services.interfaces;

import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.dto.requests.AppointmentRequestByOperator;
import com.voltmoney.scheduler.dto.response.AppointmentResponse;
import com.voltmoney.scheduler.dto.response.BaseResponse;

public interface AppointmentScheduler {

    AppointmentResponse scheduleAppointment(AppointmentRequestByOperator appointmentRequest);

    AppointmentResponse updateAppointment(AppointmentRequestByOperator appointmentRequest, Long appointmentId);

    BaseResponse cancelAppointment(Long appointmentId);
}
