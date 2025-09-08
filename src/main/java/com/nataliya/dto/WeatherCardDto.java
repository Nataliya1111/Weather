package com.nataliya.dto;

public record WeatherCardDto(
        String name,
        double temperature,
        double feelsLike,
        int humidity,
        String weatherDescription,
        String icon
) {
}
