package com.nataliya.dto;

import java.math.BigDecimal;

public record LocationCoordinatesDto(
        String name,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
