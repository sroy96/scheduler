package com.voltmoney.scheduler.exceptions;

public class ResourceNotAvailableException extends SchedulerException{
    public ResourceNotAvailableException() {
    }

    public ResourceNotAvailableException(String message) {
        super(message);
    }

    public ResourceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
