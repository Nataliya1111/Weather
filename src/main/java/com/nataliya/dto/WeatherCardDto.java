package com.nataliya.dto;

import java.math.BigDecimal;

public record WeatherCardDto(
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        double temperature,
        double feelsLike,
        int humidity,
        String weatherDescription,
        String icon
) {
}
