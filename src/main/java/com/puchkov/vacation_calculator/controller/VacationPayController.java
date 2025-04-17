package com.puchkov.vacation_calculator.controller;

import com.puchkov.vacation_calculator.dto.VacationResponseDto;
import com.puchkov.vacation_calculator.exception.IncorrectData;
import com.puchkov.vacation_calculator.exception.IncorrectPeriodDateException;
import com.puchkov.vacation_calculator.service.VacationPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
@Tag(name = "Расчет отпускных")
@Validated
public class VacationPayController {

    private final VacationPayService vacationPayService;

    @Operation(
            description = "рассчитывает отпускные по двум или трем параметрам"
    )
    @GetMapping
    public VacationResponseDto calculateVacationPay(
            @RequestParam
            @Parameter(description = "Средняя зарплата за год")
            double averageSalary,
            @RequestParam(required = false)
            @Parameter(description = "Количество дней отпуска")
            Integer vacationDays,
            @RequestParam(required = false)
            @Parameter(description = "Дата начала отпуска", example = "2023-01-10")
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
            String startDate,
            @RequestParam(required = false)
            @Parameter(description = "Дата конца отпуска", example = "2023-01-23")
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
            String endDate) {

        return vacationPayService.calculate(averageSalary, vacationDays, startDate, endDate);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(ConstraintViolationException exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getLocalizedMessage() + "(YYYY-MM-DD)");
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(IncorrectPeriodDateException exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getLocalizedMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
