package com.nataliya.dto;

import java.math.BigDecimal;

public record LocationRequestDto(
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        String country,
        String state
) {
}
