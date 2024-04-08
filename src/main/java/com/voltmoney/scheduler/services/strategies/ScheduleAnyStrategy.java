package com.voltmoney.scheduler.services.strategies;

import com.voltmoney.scheduler.dao.Appointment;
import com.voltmoney.scheduler.dao.ServiceOperator;
import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.exceptions.ResourceNotAvailableException;
import com.voltmoney.scheduler.models.Slot;
import com.voltmoney.scheduler.repository.BookingRepository;
import com.voltmoney.scheduler.repository.ServiceOperatorRepository;
import com.voltmoney.scheduler.services.interfaces.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
/**
 * Book any slot any operator
 */
public class ScheduleAnyStrategy implements Scheduler {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ServiceOperatorRepository serviceOperatorRepository;

    @Override
    public Slot scheduleAppointment(AppointmentRequest appointmentRequest) {
        LocalDate requestedDate = appointmentRequest.getSlot().getDate();
        List<Long> operators = serviceOperatorRepository.findAll().stream()
                .filter(x->x.getStatus().equals(Boolean.TRUE))
                .map(ServiceOperator::getId)
                .toList();
        for(Long id: operators){
            List<String> appointmentListForTheDate = bookingRepository.findByOperatorIdAndDate(id, requestedDate)
                    .stream().map(Appointment::getSlot).toList();
            if (appointmentListForTheDate.size() == 24) {
                log.info("SLOT_NOT_AVAILABLE_ON_THIS_DATE_FOR_THE_OPERATOR");
                continue;
            }
            else {
                for (int i = 0; i < 24; i++) {
                    String slot = i + "-" + (i + 1);
                    if (!appointmentListForTheDate.contains(slot)) {
                        return new Slot(appointmentRequest.getSlot().getDate(), i, i + 1,id);
                    }

                }
            }
        }

        throw new ResourceNotAvailableException("SLOT_NOT_AVAILABLE_ON_THIS_DATE");

    }
}
