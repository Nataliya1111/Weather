package com.nataliya.service;

import com.nataliya.dto.LocationRequestDto;
import com.nataliya.dto.UserDto;
import com.nataliya.mapper.LocationMapper;
import com.nataliya.model.Location;
import com.nataliya.model.User;
import com.nataliya.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationsService {

    private final UserService userService;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    @Transactional
    public void saveLocation(LocationRequestDto locationDto, UserDto userDto) {
        User user = userService.getByLogin(userDto.login());
        Location location = locationMapper.locationRequestDtoToLocation(locationDto, user);
        locationRepository.save(location);
    }
}
