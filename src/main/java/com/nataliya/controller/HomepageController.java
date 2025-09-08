package com.nataliya.controller;

import com.nataliya.dto.UserDto;
import com.nataliya.dto.WeatherCardDto;
import com.nataliya.service.WeatherCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.io.IOException;
import java.util.List;

@Controller("/")
@RequiredArgsConstructor
public class HomepageController {

    private static final String HOME_VIEW = "index";

    private final WeatherCardService weatherCardService;

    @GetMapping
    public String showHomepage(@RequestAttribute(value = "authUserDto", required = false) UserDto userDto,
                               Model model) throws IOException, InterruptedException {
        model.addAttribute("userDto", userDto);

        if (userDto != null) {
            List<WeatherCardDto> weatherCards = weatherCardService.getByUserDto(userDto);
            model.addAttribute("weatherCards", weatherCards);
            System.out.println(weatherCards);
        }

        return HOME_VIEW;
    }
}
