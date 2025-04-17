package com.puchkov.vacation_calculator.service;

import com.puchkov.vacation_calculator.dto.HolidayApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final RestTemplate restTemplate;
    private static final String HOLIDAY_API_URL = "https://calendar.kuzyak.in/api/calendar/%d/holidays";

    public int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidays = fetchHolidays(startDate.getYear());
        int workingDays = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            if (!isWeekend(date) && !holidays.contains(date)) {
                workingDays++;
            }
            date = date.plusDays(1);
        }

        return workingDays;
    }

    private Set<LocalDate> fetchHolidays(int year) {
        String url = String.format(HOLIDAY_API_URL, year);
        HolidayApiResponseDto response = restTemplate.getForObject(url, HolidayApiResponseDto.class);

        Set<LocalDate> holidays = new HashSet<>();
        if (response != null && response.getHolidays() != null) {
            response.getHolidays().stream()
                    .map(holiday -> LocalDate.parse(holiday.getDate().substring(0, 10)))
                    .forEach(holidays::add);
        }
        return holidays;
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}