package com.voltmoney.scheduler.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.voltmoney.scheduler.commons.Constants;
import com.voltmoney.scheduler.enums.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponse extends BaseResponse{

    @JsonIgnore
    private final ZoneId istZoneId = Constants.IST_ZONE_ID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    LocalDateTime timestamp;

    private ErrorCodes errorCode;
    private String errorMessage;

    public ErrorResponse(ErrorCodes errorCodes){
        this.timestamp = LocalDateTime.now(istZoneId);
        this.errorCode = errorCode;
    }
    

    public ErrorResponse(ErrorCodes errorCodes, String errorMessage){
        this.timestamp = LocalDateTime.now(istZoneId);
        this.errorCode = errorCodes;
        this.errorMessage =errorMessage;
    }
}
