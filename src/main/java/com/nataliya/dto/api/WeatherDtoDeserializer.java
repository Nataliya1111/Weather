package com.nataliya.dto.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

public class WeatherDtoDeserializer extends JsonDeserializer<WeatherApiResponseDto> {

    @Override
    public WeatherApiResponseDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode root = jp.getCodec().readTree(jp);
        JsonNode coordinates = root.get("coord");
        JsonNode main = root.get("main");
        JsonNode weather = root.get("weather").get(0);

        BigDecimal latitude = coordinates.get("lat").decimalValue();
        BigDecimal longitude = coordinates.get("lon").decimalValue();
        String locationName = root.get("name").asText();
        double temperature = main.get("temp").asDouble();
        double feelsLike = main.get("feels_like").asDouble();
        int humidity = main.get("humidity").asInt();
        String weatherDescription = weather.get("description").asText();
        String icon = weather.get("icon").asText();

        return new WeatherApiResponseDto(
                latitude,
                longitude,
                locationName,
                temperature,
                feelsLike,
                humidity,
                weatherDescription,
                icon
        );
    }
}