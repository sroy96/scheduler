package com.voltmoney.scheduler.commons;


public class UrlConstants {

    public final static String BASE = "/api/v1";
    public final static String BOOK_APPOINTMENT = "/appointment";
    public final static String UPDATE_APPOINTMENT = "/appointment/{appointmentId}";

    public final static String CANCEL_APPOINTMENT = "/appointment/{appointmentId}";

    public final static String OPERATOR_APPOINTMENT = "/operator";

    public final static String OPERATOR_OPEN_SLOTS = OPERATOR_APPOINTMENT + "/open-slots";
}
