package com.nataliya.integration.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya.dto.api.LocationApiResponseDto;
import com.nataliya.exception.LocationNotFoundException;
import com.nataliya.service.OpenWeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenWeatherApiServiceIT {

    private static final String LOCATIONS_JSON = """
            [
              {
                "name": "Moscow",
                "local_names": {
                  "de": "Moskau"
                },
                "lat": 55.7504461,
                "lon": 37.6174943,
                "country": "RU",
                "state": "Moscow"
              },
              {
                "name": "Moscow",
                "local_names": {
                  "en": "Moscow",
                  "ru": "Москва"
                },
                "lat": 46.7323875,
                "lon": -117.0001651,
                "country": "US",
                "state": "Idaho"
              },
              {
                "name": "Moscow",
                "lat": 45.071096,
                "lon": -69.891586,
                "country": "US",
                "state": "Maine"
              },
              {
                "name": "Moscow",
                "lat": 35.0619984,
                "lon": -89.4039612,
                "country": "US",
                "state": "Tennessee"
              },
              {
                "name": "Moscow",
                "lat": 39.5437014,
                "lon": -79.0050273,
                "country": "US",
                "state": "Maryland"
              }
            ]
            """;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @InjectMocks
    private OpenWeatherApiService openWeatherApiService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(openWeatherApiService, "weatherApiKey", "fake-key");
        ReflectionTestUtils.setField(openWeatherApiService, "unitsOfMeasurement", "metric");
    }

    @Test
    void getLocationsByName_returnsListOfLocations() throws Exception {

        when(httpResponse.statusCode()).thenReturn(HttpStatus.OK.value());
        when(httpResponse.body()).thenReturn(LOCATIONS_JSON);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        List<LocationApiResponseDto> result = openWeatherApiService.getLocationsByName("Moscow");

        assertThat(result).hasSize(5);
        assertThat(result.getFirst().name()).isEqualTo("Moscow");
        assertThat(result.getFirst().country()).isEqualTo("RU");
    }

    @Test
    void getLocationsByName_whenNotFound_shouldThrowLocationNotFound() throws Exception {

        when(httpResponse.statusCode()).thenReturn(HttpStatus.NOT_FOUND.value());
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        assertThatThrownBy(() -> openWeatherApiService.getLocationsByName("NonExistingLocation"))
                .isInstanceOf(LocationNotFoundException.class)
                .hasMessage("Location not found");
    }
}
