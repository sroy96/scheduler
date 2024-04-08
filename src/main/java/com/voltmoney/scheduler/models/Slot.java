package com.voltmoney.scheduler.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Slot {
    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @Min(value = 0, message = "Start Time must be at least 0")
    @Max(value = 24, message = "Start Time must be at most 24")
    private Integer startTime;

    @Min(value = 0, message = "End Time must be at least 0")
    @Max(value = 24, message = "End Time must be at most 24")
    private Integer endTime;

    private Long operatorId;

}
