package com.nataliya.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record LocationApiResponseDto(
        String name,
        @JsonProperty("lat") BigDecimal latitude,
        @JsonProperty("lon") BigDecimal longitude,
        String country,
        String state
) {
}
