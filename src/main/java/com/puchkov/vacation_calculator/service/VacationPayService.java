package com.puchkov.vacation_calculator.service;

import com.puchkov.vacation_calculator.dto.VacationResponseDto;
import com.puchkov.vacation_calculator.exception.IncorrectPeriodDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VacationPayService {

    private static final double AVERAGE_DAYS_IN_MONTH = 29.3;
    private final HolidayService holidayService;

    public VacationResponseDto calculate(double averageSalary, Integer vacationDays, String startDate, String endDate) {
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            if(start.isAfter(end)){
                throw new IncorrectPeriodDateException("Дата начала отпуска должна быть раньше конца отпуска");
            }
            int workingDays = holidayService.calculateWorkingDays(start, end);
            double pay = calculatePay(averageSalary, workingDays);
            return new VacationResponseDto(pay);
        } else if (vacationDays != null) {
            //todo проверить на отрицательное число
            double pay = calculatePay(averageSalary, vacationDays);
            return new VacationResponseDto(pay);
        } else {
            throw new IllegalArgumentException("Необходимо указать либо количество дней отпуска, либо даты начала/окончания");//todo поймать это исключение
        }
    }

    private double calculatePay(double averageSalary, int days) {
        double dailySalary = averageSalary / AVERAGE_DAYS_IN_MONTH;
        return Math.round(dailySalary * days * 100.0) / 100.0;
    }
}
