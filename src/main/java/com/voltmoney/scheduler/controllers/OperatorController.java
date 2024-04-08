package com.voltmoney.scheduler.controllers;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.voltmoney.scheduler.commons.UrlConstants;
import com.voltmoney.scheduler.dao.ServiceOperator;
import com.voltmoney.scheduler.dto.response.BaseResponse;
import com.voltmoney.scheduler.services.OperatorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping(UrlConstants.BASE)
public class OperatorController {
    @Autowired
    OperatorServiceImpl operatorService;

    @GetMapping(UrlConstants.OPERATOR_APPOINTMENT)
    public ResponseEntity<BaseResponse> getOperatorAppointment(@RequestParam Long operatorId, @RequestParam LocalDate date){
        return new ResponseEntity<>(operatorService.bookedSlots(operatorId,date), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.OPERATOR_OPEN_SLOTS)
    public ResponseEntity<BaseResponse> getOperatorOpenSlots(@RequestParam Long operatorId, @RequestParam LocalDate date){
        return new ResponseEntity<>(operatorService.openSlots(operatorId, date), HttpStatus.OK);
    }

    /**
     * Utility endpoint
     * @param serviceOperator
     * @return
     */
    @PostMapping("/operator")
    public ResponseEntity<BaseResponse>addOperator(@RequestBody ServiceOperator serviceOperator){
        return new ResponseEntity<>(operatorService.addOperator(serviceOperator), HttpStatus.OK);

    }
}

