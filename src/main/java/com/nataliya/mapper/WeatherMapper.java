package com.nataliya.mapper;

import com.nataliya.dto.WeatherCardDto;
import com.nataliya.dto.api.WeatherApiResponseDto;
import com.nataliya.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeatherMapper {

    @Mapping(target = "name", expression = "java(location.getName())")
    @Mapping(target = "latitude", expression = "java(location.getLatitude())")
    @Mapping(target = "longitude", expression = "java(location.getLongitude())")
    WeatherCardDto weatherApiResponseDtoToWeatherCardDto(
            WeatherApiResponseDto weatherApiResponseDto,
            Location location
    );
}
