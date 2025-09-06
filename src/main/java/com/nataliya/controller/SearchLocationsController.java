package com.nataliya.controller;

import com.nataliya.dto.LocationApiResponseDto;
import com.nataliya.dto.UserDto;
import com.nataliya.service.OpenWeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/search-results")
@RequiredArgsConstructor
public class SearchLocationsController {

    private static final String SEARCH_VIEW = "search-results";
    private static final String HOME_REDIRECT = "redirect:/";
    private static final String SEARCH_REDIRECT = "redirect:/search-results";

    private final OpenWeatherApiService openWeatherApiService;

    @GetMapping
    public String showSearchPage(@RequestAttribute(value = "authUserDto") UserDto userDto,
                                 @RequestParam(required = false) String locationQuery,
                                 Model model) throws IOException, InterruptedException {

        model.addAttribute("userDto", userDto);

        if (locationQuery == null || locationQuery.isBlank()) {
            model.addAttribute("locationQueryErrorMessage", "Location name must not be blank");
            return SEARCH_VIEW;
        }

        locationQuery = locationQuery.trim();
        List<LocationApiResponseDto> searchedLocations = openWeatherApiService.getLocationsByName(locationQuery);
        model.addAttribute("locationQuery", locationQuery);

        if (searchedLocations.isEmpty()) {
            model.addAttribute("locationQueryErrorMessage", "Locations with such names are not found");
            return SEARCH_VIEW;
        }

        model.addAttribute("locations", searchedLocations);

        return SEARCH_VIEW;
    }


}
