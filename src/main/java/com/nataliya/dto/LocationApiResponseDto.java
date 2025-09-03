package com.nataliya.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationApiResponseDto(
        String name,
        @JsonProperty("lat") String latitude,
        @JsonProperty("lon") String longitude,
        String country,
        String state
) {
}
