package com.voltmoney.scheduler.handlers.exception;

import com.voltmoney.scheduler.dto.response.ErrorResponse;
import com.voltmoney.scheduler.enums.ErrorCodes;
import com.voltmoney.scheduler.exceptions.BadRequest;
import com.voltmoney.scheduler.exceptions.ResourceNotAvailableException;
import com.voltmoney.scheduler.exceptions.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandlers {

    @ExceptionHandler({
            SchedulerException.class
    })
    public ResponseEntity<ErrorResponse> applicationExceptions(Exception e){
        return new ResponseEntity<>(new ErrorResponse(ErrorCodes.UNKNOW_ERROR,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            ResourceNotAvailableException.class
    })
    public ResponseEntity<ErrorResponse> resourceNotAvailableExceptions(Exception e){
        return new ResponseEntity<>(new ErrorResponse(ErrorCodes.REQUESTED_RESOURCE_NOT_AVAILABLE,e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler({
            BadRequest.class
    })
    public ResponseEntity<ErrorResponse> badRequest(Exception e){
        return new ResponseEntity<>(new ErrorResponse(ErrorCodes.BAD_REQUEST,e.getMessage()), HttpStatus.BAD_REQUEST);
    }


}
