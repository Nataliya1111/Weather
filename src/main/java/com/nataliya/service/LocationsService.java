package com.nataliya.service;

import com.nataliya.dto.LocationRequestDto;
import com.nataliya.dto.UserDto;
import com.nataliya.exception.DuplicateLocationException;
import com.nataliya.exception.TooManyLocationsException;
import com.nataliya.mapper.LocationMapper;
import com.nataliya.model.Location;
import com.nataliya.model.User;
import com.nataliya.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class LocationsService {

    private static final int MAX_LOCATIONS_NUMBER = 10;

    private final UserService userService;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    @Transactional
    public void saveLocation(LocationRequestDto locationDto, UserDto userDto) {

        User user = userService.getByLogin(userDto.login());
        ensureLocationCanBeSaved(user, locationDto);

        Location location = locationMapper.locationRequestDtoToLocation(locationDto, user);
        locationRepository.save(location);
    }

    private void ensureLocationCanBeSaved(User user, LocationRequestDto locationDto) {
        long locationsCount = locationRepository.countByUser(user);
        if (locationsCount >= MAX_LOCATIONS_NUMBER) {
            throw new TooManyLocationsException(
                    String.format("User cannot have more than %s added locations", MAX_LOCATIONS_NUMBER)
            );
        }
        boolean isLocationExists = locationRepository.existsByUserAndLatitudeAndLongitude(
                user,
                locationDto.latitude().setScale(6, RoundingMode.HALF_UP),
                locationDto.longitude().setScale(6, RoundingMode.HALF_UP)
        );
        if (isLocationExists) {
            throw new DuplicateLocationException("This location is already added");
        }
    }
}
