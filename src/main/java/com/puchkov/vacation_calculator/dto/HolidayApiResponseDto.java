package com.puchkov.vacation_calculator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayApiResponseDto {
    private List<Holiday> holidays;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Holiday {
        private String date;
    }
}
