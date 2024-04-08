package com.voltmoney.scheduler.services;

import com.voltmoney.scheduler.dao.Appointment;
import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.dto.requests.AppointmentRequestByOperator;
import com.voltmoney.scheduler.dto.response.AppointmentResponse;
import com.voltmoney.scheduler.dto.response.BaseResponse;
import com.voltmoney.scheduler.exceptions.BadRequest;
import com.voltmoney.scheduler.models.Slot;
import com.voltmoney.scheduler.repository.BookingRepository;
import com.voltmoney.scheduler.services.interfaces.AppointmentScheduler;
import com.voltmoney.scheduler.services.strategies.ScheduleAnyStrategy;
import com.voltmoney.scheduler.services.strategies.ScheduleOperatorAppointment;
import com.voltmoney.scheduler.services.strategies.ScheduleRequestedSlotAnyOperator;
import com.voltmoney.scheduler.services.strategies.ScheduleRequestedSlotByOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;


@Service
public class AppointmentServiceImpl implements AppointmentScheduler {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ScheduleAnyStrategy scheduleAnyStrategy;

    @Autowired
    ScheduleOperatorAppointment scheduleOperatorAppointment;

    @Autowired
    ScheduleRequestedSlotByOperator scheduleRequestedSlotByOperator;

    @Autowired
    ScheduleRequestedSlotAnyOperator scheduleRequestedSlotAnyOperator;

    private void validateRequest(AppointmentRequest appointmentRequest) {
        if (appointmentRequest.getSlot().getEndTime() - appointmentRequest.getSlot().getStartTime() != 1) {
            if (!(appointmentRequest.getSlot().getEndTime() == 0 && appointmentRequest.getSlot().getStartTime() == 23)) {
                throw new BadRequest("ONLY_ONE_HOUR_SLOT_CAN_BE_BOOKED");
            }
        }
    }


    @Override
    public AppointmentResponse scheduleAppointment(AppointmentRequestByOperator appointmentRequestByOperator) {

        Slot allocatedSlot = null;
        AppointmentRequest appointmentRequest = null;



        if (null!=appointmentRequestByOperator.getOperatorId()) {
            appointmentRequest =  appointmentRequestByOperator;
            if (appointmentRequest.getSlot().getStartTime()!=null && appointmentRequest.getSlot().getEndTime()!=null) {
                validateRequest(appointmentRequest);
                allocatedSlot = scheduleRequestedSlotByOperator.scheduleAppointment(appointmentRequest);
            } else {
                allocatedSlot = scheduleOperatorAppointment.scheduleAppointment(appointmentRequest);
            }
        } else {
            appointmentRequest =(AppointmentRequest) appointmentRequestByOperator;
            if (appointmentRequest.getSlot().getStartTime()!=null && appointmentRequest.getSlot().getEndTime()!=null) {
                validateRequest(appointmentRequest);
                allocatedSlot = scheduleRequestedSlotAnyOperator.scheduleAppointment(appointmentRequest);
            } else {
                allocatedSlot = scheduleAnyStrategy.scheduleAppointment(appointmentRequest);
            }
        }
        Long appointmentId = new Random().nextLong();
        bookingRepository.save(new Appointment(appointmentId,
                (allocatedSlot.getStartTime() + "-" + allocatedSlot.getEndTime()),
                appointmentRequest.getSlot().getDate(),
                allocatedSlot.getOperatorId()));

        return new AppointmentResponse(appointmentId, allocatedSlot);


    }

    @Override
    public AppointmentResponse updateAppointment(AppointmentRequestByOperator appointmentRequest, Long appointmentId) {
        if (appointmentId == null) {
            throw new BadRequest("APPOINTMENT_ID_REQUIRED");
        }
        Optional<Appointment> appointment = bookingRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("NO_SUCH_APPOINTMENT_PRESENT");
        }


        Slot allocatedSlot = null;
        if (null!=appointmentRequest.getOperatorId()) {
            if (appointmentRequest.getSlot().getStartTime() != null && appointmentRequest.getSlot().getEndTime() != null) {
                validateRequest(appointmentRequest);
                allocatedSlot = scheduleRequestedSlotByOperator.scheduleAppointment(appointmentRequest);
            } else {
                allocatedSlot = scheduleOperatorAppointment.scheduleAppointment(appointmentRequest);
            }
        } else {
            if (appointmentRequest.getSlot().getStartTime() != null && appointmentRequest.getSlot().getEndTime() != null) {
                validateRequest(appointmentRequest);
                allocatedSlot = scheduleRequestedSlotAnyOperator.scheduleAppointment(appointmentRequest);
            } else {
                allocatedSlot = scheduleAnyStrategy.scheduleAppointment(appointmentRequest);
            }
        }

        bookingRepository.save(new Appointment(appointmentId,
                (allocatedSlot.getStartTime() + "-" + allocatedSlot.getEndTime()),
                appointmentRequest.getSlot().getDate(),
                allocatedSlot.getOperatorId()));

        return new AppointmentResponse(appointmentId, allocatedSlot);
    }

    @Override
    public BaseResponse cancelAppointment(Long appointmentId) {
        Optional<Appointment> appointment = bookingRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("NO_SUCH_APPOINTMENT_PRESENT");
        }
        bookingRepository.deleteById(appointmentId);
        return new BaseResponse();
    }
}
