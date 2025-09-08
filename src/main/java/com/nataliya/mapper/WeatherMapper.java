package com.nataliya.mapper;

import com.nataliya.dto.WeatherCardDto;
import com.nataliya.dto.api.WeatherApiResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeatherMapper {

    @Mapping(target = "name", source = "locationName")
    WeatherCardDto weatherApiResponseDtoToWeatherCardDto(
            WeatherApiResponseDto weatherApiResponseDto,
            String locationName
    );
}
