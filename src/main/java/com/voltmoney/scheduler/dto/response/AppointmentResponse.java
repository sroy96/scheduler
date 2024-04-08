package com.voltmoney.scheduler.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.voltmoney.scheduler.models.Slot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AppointmentResponse extends BaseResponse{

    private Long appointmentId;
    private Slot slot;

}
