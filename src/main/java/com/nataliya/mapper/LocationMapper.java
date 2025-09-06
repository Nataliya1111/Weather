package com.nataliya.mapper;

import com.nataliya.dto.LocationRequestDto;
import com.nataliya.model.Location;
import com.nataliya.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "name", expression = "java(buildName(dto))")
    Location locationRequestDtoToLocation(LocationRequestDto dto, User user);

    default String buildName(LocationRequestDto dto) {
        StringBuilder name = new StringBuilder(dto.name());
        if (dto.country() != null && !dto.country().isBlank()) {
            name.append(", ").append(dto.country());
        }
        if (dto.state() != null && !dto.state().isBlank() && !dto.state().equals(dto.name())) {
            name.append(", ");
            if (dto.country().equals("US")) {
                name.append("State of ");
            }
            name.append(dto.state());
        }
        return name.toString();
    }

}
