package com.nataliya.mapper;

import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);
}

