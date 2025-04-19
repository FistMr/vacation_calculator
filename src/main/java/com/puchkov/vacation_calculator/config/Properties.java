package com.puchkov.vacation_calculator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "holiday.api")
@Getter
@Setter
public class Properties {
    private String url;
}
