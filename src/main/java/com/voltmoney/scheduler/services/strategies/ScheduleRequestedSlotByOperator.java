package com.voltmoney.scheduler.services.strategies;

import com.voltmoney.scheduler.dao.Appointment;
import com.voltmoney.scheduler.dao.ServiceOperator;
import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.dto.requests.AppointmentRequestByOperator;
import com.voltmoney.scheduler.exceptions.ResourceNotAvailableException;
import com.voltmoney.scheduler.models.Slot;
import com.voltmoney.scheduler.repository.BookingRepository;
import com.voltmoney.scheduler.repository.ServiceOperatorRepository;
import com.voltmoney.scheduler.services.interfaces.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleRequestedSlotByOperator implements Scheduler {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ServiceOperatorRepository serviceOperatorRepository;

    private void validate(Long operatorId) {
        Optional<ServiceOperator> serviceOperator = serviceOperatorRepository.findById(operatorId);
        if (serviceOperator.isPresent()) {
            if (!serviceOperator.get().getStatus()) {
                throw new ResourceNotAvailableException("OPERATOR_IS_INACTIVE");
            }

        } else {
            throw new ResourceNotAvailableException("OPERATOR_NOT_PRESENT");

        }

    }

    @Override
    public Slot scheduleAppointment(AppointmentRequest appointmentRequest) {
        LocalDate requestedDate = appointmentRequest.getSlot().getDate();
        String requestedSlot = appointmentRequest.getSlot().getStartTime() + "-" + appointmentRequest.getSlot().getEndTime();
        AppointmentRequestByOperator appointmentRequestByOperator = (AppointmentRequestByOperator) appointmentRequest;
        validate(appointmentRequestByOperator.getOperatorId());
        List<Appointment> appointmentListForTheDate = bookingRepository.findByOperatorIdAndDate(appointmentRequestByOperator.getOperatorId(), requestedDate);
        if (appointmentListForTheDate.size() == 24) {
            throw new ResourceNotAvailableException("SLOT_NOT_AVAILABLE_ON_THIS_DATE_FOR_THE_OPERATOR");
        }
        List<String> appointmentListForTheDateAndSlot = bookingRepository.findByOperatorIdAndDateAndSlot(appointmentRequestByOperator.getOperatorId(), requestedDate, requestedSlot)
                .stream().map(Appointment::getSlot).toList();
        if (!appointmentListForTheDateAndSlot.contains(requestedSlot)) {
            return new Slot(requestedDate,  appointmentRequest.getSlot().getStartTime(), appointmentRequest.getSlot().getEndTime(), appointmentRequestByOperator.getOperatorId());
        }

        throw new ResourceNotAvailableException("SLOT_NOT_AVAILABLE_ON_THIS_DATE_SLOT");

    }
}
