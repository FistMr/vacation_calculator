package com.puchkov.vacation_calculator.service;

import com.puchkov.vacation_calculator.config.Properties;
import com.puchkov.vacation_calculator.dto.HolidayApiResponseDto;
import com.puchkov.vacation_calculator.exception.IncorrectDateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayService {

    private final RestTemplate restTemplate;

    private final Properties apiProperties;

    public int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating working days from {} to {}", startDate, endDate);

        Set<LocalDate> holidays = fetchHolidays(startDate.getYear());
        int workingDays = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            if (!isWeekend(date) && !holidays.contains(date)) {
                workingDays++;
            }
            date = date.plusDays(1);
        }

        log.debug("Total working days calculated: {}", workingDays);
        return workingDays;
    }

    private Set<LocalDate> fetchHolidays(int year) {
        log.debug("Fetching holidays for year {}", year);

        String url = String.format(apiProperties.getUrl(), year);
        HolidayApiResponseDto response = null;
        try {
            response = restTemplate.getForObject(url, HolidayApiResponseDto.class);
        } catch (HttpClientErrorException exception) {
            log.error("Failed to fetch holidays for year {}: {}", year, exception.getMessage());
            if (exception.getLocalizedMessage().contains("Invalid year")) {
                throw new IncorrectDateException("Расчет для указанного года " + year + " недоступен. Пожалуйста, выберите год между 2023 и 2025.");
            }
        }

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