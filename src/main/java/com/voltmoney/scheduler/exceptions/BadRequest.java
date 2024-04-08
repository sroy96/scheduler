package com.voltmoney.scheduler.exceptions;

public class BadRequest extends SchedulerException{
    public BadRequest() {
    }

    public BadRequest(String message) {
        super(message);
    }

    public BadRequest(String message, Throwable cause) {
        super(message, cause);
    }
}
