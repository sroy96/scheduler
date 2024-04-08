package com.voltmoney.scheduler.exceptions;

import lombok.extern.slf4j.Slf4j;


public class SchedulerException extends RuntimeException{
    public SchedulerException() {
    }

    public SchedulerException(String message) {
        super(message);

    }

    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
}
