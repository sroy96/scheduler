package com.voltmoney.scheduler.services.interfaces;

import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.models.Slot;

public interface Scheduler {

    Slot scheduleAppointment(AppointmentRequest appointmentRequest);

}
