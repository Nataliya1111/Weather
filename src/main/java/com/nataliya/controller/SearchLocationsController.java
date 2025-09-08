package com.nataliya.controller;

import com.nataliya.dto.LocationRequestDto;
import com.nataliya.dto.api.LocationApiResponseDto;
import com.nataliya.dto.UserDto;
import com.nataliya.exception.DuplicateLocationException;
import com.nataliya.exception.TooManyLocationsException;
import com.nataliya.service.LocationsService;
import com.nataliya.service.OpenWeatherApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/search-results")
@RequiredArgsConstructor
@Slf4j
public class SearchLocationsController {

    private static final String SEARCH_VIEW = "search-results";
    private static final String SEARCH_RESULTS_REDIRECT = "redirect:/search-results";
    private static final String HOME_REDIRECT = "redirect:/";

    private final OpenWeatherApiService openWeatherApiService;
    private final LocationsService locationsService;

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

    @PostMapping
    public String addUserLocation(@RequestAttribute(value = "authUserDto") UserDto userDto,
                                  @ModelAttribute(value = "location") LocationRequestDto locationDto,
                                  @RequestParam String locationQuery,
                                  RedirectAttributes redirectAttributes) {

        try {
            locationsService.saveLocation(locationDto, userDto);
        } catch (TooManyLocationsException | DuplicateLocationException ex) {
            log.info((ex.getClass().equals(TooManyLocationsException.class))
                    ? "Attempt to save too many locations fore one user. {}"
                    : "Attempt to create duplicate location. {}"
                    , ex.getMessage());
            redirectAttributes.addAttribute("locationQuery", locationQuery);
            redirectAttributes.addFlashAttribute("saveLocationErrorMessage", ex.getMessage());
            return SEARCH_RESULTS_REDIRECT;
        }

        return HOME_REDIRECT;
    }

}
