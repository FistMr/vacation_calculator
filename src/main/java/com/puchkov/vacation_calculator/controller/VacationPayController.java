package com.puchkov.vacation_calculator.controller;

import com.puchkov.vacation_calculator.dto.VacationResponseDto;
import com.puchkov.vacation_calculator.service.VacationPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
@Tag(name = "Расчет отпускных")
@Validated
@Slf4j
public class VacationPayController {

    private final VacationPayService vacationPayService;

    @Operation(description = "рассчитывает отпускные по двум или трем параметрам")
    @GetMapping
    public VacationResponseDto calculateVacationPay(
            @RequestParam
            @Parameter(description = "Средняя зарплата за год")
            @Min(1)
            double averageSalary,
            @RequestParam(required = false)
            @Parameter(description = "Количество дней отпуска")
            @Min(1)
            Integer vacationDays,
            @RequestParam(required = false)
            @Parameter(description = "Дата начала отпуска", example = "2023-01-10")
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
            String startDate,
            @RequestParam(required = false)
            @Parameter(description = "Дата конца отпуска", example = "2023-01-23")
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
            String endDate) {
        log.info("Received vacation pay calculation request - averageSalary: {}, vacationDays: {}, startDate: {}, endDate: {}",
                averageSalary, vacationDays, startDate, endDate);
        return vacationPayService.calculate(averageSalary, vacationDays, startDate, endDate);
    }
}
