package com.nataliya.controller;

import com.nataliya.dto.LocationApiResponseDto;
import com.nataliya.dto.UserDto;
import com.nataliya.service.OpenWeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
                               Model model) {

        model.addAttribute("userDto", userDto);

        return SEARCH_VIEW;
    }

    @PostMapping
    public String searchLocations(@ModelAttribute("locationNameToSearch") String locationNameToSearch) throws IOException, InterruptedException {

        System.out.println("Location: " + locationNameToSearch);

        List<LocationApiResponseDto> locationsByName = openWeatherApiService.getLocationsByName(locationNameToSearch);
        System.out.println(locationsByName);



        return SEARCH_REDIRECT;
    }
}
