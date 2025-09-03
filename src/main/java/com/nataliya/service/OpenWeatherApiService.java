package com.nataliya.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya.dto.LocationApiResponseDto;
import com.nataliya.dto.WeatherApiResponseDto;
import com.nataliya.exception.LocationNotFoundException;
import com.nataliya.exception.WeatherApiResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenWeatherApiService {

    private static final String BASE_GEOCODING_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String BASE_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final int LOCATIONS_NUMBER_LIMIT = 5;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${WEATHER_API_KEY}")
    private String weatherApiKey;

    @Value("${weather.api.units.of.measurement}")
    private String unitsOfMeasurement;

    public List<LocationApiResponseDto> getLocationsByName(String locationName) throws IOException, InterruptedException {

        URI uri = UriComponentsBuilder
                .fromUriString(BASE_GEOCODING_URL)
                .queryParam("q", locationName)
                .queryParam("limit", LOCATIONS_NUMBER_LIMIT)
                .queryParam("appid", weatherApiKey)
                .encode()
                .build()
                .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        validateResponse(response, uri);

        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    public WeatherApiResponseDto getWeatherByLocation(LocationApiResponseDto locationApiResponseDto) throws IOException, InterruptedException {

        URI uri = UriComponentsBuilder
                .fromUriString(BASE_WEATHER_URL)
                .queryParam("lat", locationApiResponseDto.latitude())
                .queryParam("lon", locationApiResponseDto.longitude())
                .queryParam("units", unitsOfMeasurement)
                .queryParam("appid", weatherApiKey)
                .encode()
                .build()
                .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        validateResponse(response, uri);

        return objectMapper.readValue(response.body(), WeatherApiResponseDto.class);
    }

    private void validateResponse(HttpResponse<String> response, URI uri) {
        HttpStatus status = HttpStatus.resolve(response.statusCode());
        if (status == null) {
            throw new WeatherApiResponseException(
                    "Unknown HTTP status: " + response.statusCode(), null
            );
        }
        if (!status.isError()) {
            return;
        }

        if (status == HttpStatus.NOT_FOUND & uri.toString().startsWith(BASE_GEOCODING_URL)) {
            throw new LocationNotFoundException("Location not found");
        } else {
            throw new WeatherApiResponseException(
                    String.format("Weather API call '%s' failed with status: %d - %s.", uri, status.value(), status.getReasonPhrase()),
                    status
            );
        }
    }
}
