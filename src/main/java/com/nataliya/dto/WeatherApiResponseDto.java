package com.nataliya.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize(using = WeatherDtoDeserializer.class)
public record WeatherApiResponseDto(
        BigDecimal latitude,
        BigDecimal longitude,
        String locationName,
        double temperature,
        double feelsLike,
        int humidity,
        String weatherDescription,
        String icon
) {
}
