package com.nataliya.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya.dto.LocationDto;
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
    private static final int LOCATIONS_NUMBER_LIMIT = 5;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${WEATHER_API_KEY}")
    private String weatherApiKey;

    public List<LocationDto> getLocationsByName(String locationName) throws IOException, InterruptedException {

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

        HttpStatus status = HttpStatus.resolve(response.statusCode());
        if (status.isError()) {
            handleErrorResponse(status, uri);
        }
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private void handleErrorResponse(HttpStatus status, URI uri) {
        if (status == HttpStatus.NOT_FOUND) {
            throw new LocationNotFoundException("Location not found");
        } else {
            throw new WeatherApiResponseException(
                    String.format("Weather API call '%s' failed with status: %d - %s.", uri, status.value(), status.getReasonPhrase()),
                    status
            );
        }
    }
}
