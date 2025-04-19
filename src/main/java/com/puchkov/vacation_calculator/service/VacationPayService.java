package com.puchkov.vacation_calculator.service;

import com.puchkov.vacation_calculator.dto.VacationResponseDto;
import com.puchkov.vacation_calculator.exception.IncorrectDateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationPayService {

    private static final double AVERAGE_DAYS_IN_YEAR = 247;
    private final HolidayService holidayService;

    public VacationResponseDto calculate(double averageSalary, Integer vacationDays, String startDate, String endDate) {
        log.debug("Starting calculation with params: salary={}, vacationDays={}, startDate={}, endDate={}",
                averageSalary, vacationDays, startDate, endDate);

        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if(start.isAfter(end)){
                log.error("Invalid date range: start date {} is after end date {}", startDate, endDate);
                throw new IncorrectDateException("Дата начала отпуска должна быть раньше даты конца отпуска");
            }

            int workingDays = holidayService.calculateWorkingDays(start, end);
            double pay = calculatePay(averageSalary, workingDays);
            return new VacationResponseDto(pay);
        } else if (vacationDays != null) {
            double pay = calculatePay(averageSalary, vacationDays);
            return new VacationResponseDto(pay);
        } else {
            log.error("Missing required parameters - either vacationDays or startDate/endDate must be provided");
            throw new IllegalArgumentException("Необходимо указать либо количество дней отпуска, либо даты начала/окончания");
        }
    }

    private double calculatePay(double averageSalary, int days) {
        double dailySalary = averageSalary / AVERAGE_DAYS_IN_YEAR;
        double pay = Math.round(dailySalary * days * 100.0) / 100.0;
        log.info("Calculated daily salary: {}, pay for {} days: {}", dailySalary, days, pay);
        return pay;
    }
}
