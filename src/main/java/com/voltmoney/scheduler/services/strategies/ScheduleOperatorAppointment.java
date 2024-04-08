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
/**
 * Book any slot by operator
 */
public class ScheduleOperatorAppointment implements Scheduler {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ServiceOperatorRepository serviceOperatorRepository;

    private void validate(Long operatorId){
        Optional<ServiceOperator> serviceOperator = serviceOperatorRepository.findById(operatorId);
        if(serviceOperator.isPresent()){
            if(!serviceOperator.get().getStatus()){
                throw new ResourceNotAvailableException("OPERATOR_IS_INACTIVE");
            }

        }
        else{
            throw new ResourceNotAvailableException("OPERATOR_NOT_PRESENT");

        }

    }
    @Override
    public Slot scheduleAppointment(AppointmentRequest appointmentRequest) {
        AppointmentRequestByOperator appointmentRequestByOperator = (AppointmentRequestByOperator) appointmentRequest;
        LocalDate requestedDate = appointmentRequest.getSlot().getDate();
        validate(appointmentRequestByOperator.getOperatorId());
        List<Appointment> appointmentListForTheDate = bookingRepository.findByOperatorIdAndDate(appointmentRequestByOperator.getOperatorId(), requestedDate);
        if (appointmentListForTheDate.size() == 24) {
            throw new ResourceNotAvailableException("SLOT_NOT_AVAILABLE_ON_THIS_DATE_FOR_THE_OPERATOR");
        }
        List<String> bookedSlots = appointmentListForTheDate.stream()
                .map(Appointment::getSlot)
                .toList();
        for (int i = 0; i < 24; i++) {
            String slot = i + "-" + (i + 1);
            if (!bookedSlots.contains(slot)) {
                return new Slot(appointmentRequest.getSlot().getDate(), i, i + 1,appointmentRequestByOperator.getOperatorId());
            }

        }
        throw new ResourceNotAvailableException("SLOT_NOT_AVAILABLE_ON_THIS_DATE_FOR_THE_OPERATOR");

    }

}
