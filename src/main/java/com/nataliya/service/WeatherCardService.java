package com.nataliya.service;

import com.nataliya.dto.UserDto;
import com.nataliya.dto.WeatherCardDto;
import com.nataliya.dto.api.WeatherApiResponseDto;
import com.nataliya.mapper.WeatherMapper;
import com.nataliya.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherCardService {

    private final LocationsService locationsService;
    private final OpenWeatherApiService openWeatherApiService;
    private final WeatherMapper weatherMapper;

    public List<WeatherCardDto> getByUserDto(UserDto userDto) throws IOException, InterruptedException {
        List<Location> locations = locationsService.getByUserDto(userDto);

        List<WeatherCardDto> weatherCards = new ArrayList<>();
        for (Location location : locations) {
            WeatherCardDto weatherCardDto = getWeatherCard(location);
            weatherCards.add(weatherCardDto);
        }
        return weatherCards;
    }

    private WeatherCardDto getWeatherCard(Location location) throws IOException, InterruptedException {
        WeatherApiResponseDto weatherDto = openWeatherApiService
                .getWeatherByLocation(location.getLatitude(), location.getLongitude());
        return weatherMapper.weatherApiResponseDtoToWeatherCardDto(weatherDto, location);
    }
}
