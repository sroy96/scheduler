package com.voltmoney.scheduler.services.strategies;

import com.voltmoney.scheduler.dao.Appointment;
import com.voltmoney.scheduler.dao.ServiceOperator;
import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.exceptions.ResourceNotAvailableException;
import com.voltmoney.scheduler.models.Slot;
import com.voltmoney.scheduler.repository.BookingRepository;
import com.voltmoney.scheduler.repository.ServiceOperatorRepository;
import com.voltmoney.scheduler.services.interfaces.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ScheduleRequestedSlotAnyOperator implements Scheduler {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ServiceOperatorRepository serviceOperatorRepository;

    @Override
    public Slot scheduleAppointment(AppointmentRequest appointmentRequest) {
        LocalDate requestedDate = appointmentRequest.getSlot().getDate();
        String requestedSlot = appointmentRequest.getSlot().getStartTime() + "-" + appointmentRequest.getSlot().getEndTime();
        List<Long> operators = serviceOperatorRepository.findAll().stream()
                .filter(x -> x.getStatus().equals(Boolean.TRUE))
                .map(ServiceOperator::getId)
                .toList();
        for (Long operatorId : operators) {
            List<String> appointmentListForTheDateAndSlot = bookingRepository.findByOperatorIdAndDateAndSlot(operatorId, requestedDate, requestedSlot)
                    .stream().map(Appointment::getSlot).toList();
            if (appointmentListForTheDateAndSlot.isEmpty()) {
                return new Slot(appointmentRequest.getSlot().getDate(), appointmentRequest.getSlot().getStartTime(), appointmentRequest.getSlot().getEndTime(), operatorId);
            }
        }
        throw new ResourceNotAvailableException("SLOT_NOT_AVAILABLE_ON_THIS_DATE_SLOT");

    }
}
