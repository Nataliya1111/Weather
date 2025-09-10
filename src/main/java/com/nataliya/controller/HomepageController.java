package com.nataliya.controller;

import com.nataliya.dto.LocationCoordinatesDto;
import com.nataliya.dto.UserDto;
import com.nataliya.dto.WeatherCardDto;
import com.nataliya.service.LocationsService;
import com.nataliya.service.WeatherCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.io.IOException;
import java.util.List;

@Controller("/")
@RequiredArgsConstructor
public class HomepageController {

    private static final String HOME_VIEW = "index";
    private static final String HOME_REDIRECT = "redirect:/";

    private final WeatherCardService weatherCardService;
    private final LocationsService locationsService;

    @GetMapping
    public String showHomepage(@RequestAttribute(value = "authUserDto", required = false) UserDto userDto,
                               Model model) throws IOException, InterruptedException {
        model.addAttribute("userDto", userDto);

        if (userDto != null) {
            List<WeatherCardDto> weatherCards = weatherCardService.getByUserDto(userDto);
            model.addAttribute("weatherCards", weatherCards);
        }

        return HOME_VIEW;
    }

    @DeleteMapping
    public String deleteLocation(@ModelAttribute(value = "locationCoordinatesDto") LocationCoordinatesDto locationCoordinatesDto,
                                 @RequestAttribute(value = "authUserDto", required = false) UserDto userDto) {

        if (userDto == null) {
            return HOME_REDIRECT;
        }
        locationsService.deleteLocation(locationCoordinatesDto, userDto);
        return HOME_REDIRECT;
    }
}
