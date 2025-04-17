package com.puchkov.vacation_calculator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Vacation Calculator",
                description = "API для расчета отпускных",
                version = "1.0.0",
                contact = @Contact(
                        name = "Puchkov Pavel",
                        email = "pavel.pu4ckow@yandex.ru"
                )
        )
)
public class OpenApiConfig {
}
