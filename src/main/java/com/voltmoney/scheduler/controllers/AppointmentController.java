package com.voltmoney.scheduler.controllers;

import com.voltmoney.scheduler.commons.UrlConstants;
import com.voltmoney.scheduler.dto.requests.AppointmentRequest;
import com.voltmoney.scheduler.dto.requests.AppointmentRequestByOperator;
import com.voltmoney.scheduler.dto.response.BaseResponse;
import com.voltmoney.scheduler.services.AppointmentServiceImpl;
import com.voltmoney.scheduler.services.interfaces.AppointmentScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstants.BASE)
public class AppointmentController {

    @Autowired
    AppointmentServiceImpl appointmentScheduler;



    @PostMapping(UrlConstants.BOOK_APPOINTMENT)
    public ResponseEntity<BaseResponse> bookAppointment(@RequestBody AppointmentRequestByOperator appointmentRequest) {
        return new ResponseEntity<>(appointmentScheduler.scheduleAppointment(appointmentRequest), HttpStatus.OK);
    }

    @PutMapping(UrlConstants.UPDATE_APPOINTMENT)
    public ResponseEntity<BaseResponse> updateAppointment(@RequestBody AppointmentRequestByOperator appointmentRequest, @RequestParam Long appointmentId) {
        return new ResponseEntity<>(appointmentScheduler.updateAppointment(appointmentRequest, appointmentId), HttpStatus.OK);
    }

    @DeleteMapping(UrlConstants.CANCEL_APPOINTMENT)
    public ResponseEntity<BaseResponse> cancelAppointment(@PathVariable Long appointmentId) {
        return new ResponseEntity<>(appointmentScheduler.cancelAppointment(appointmentId), HttpStatus.OK);
    }
}
