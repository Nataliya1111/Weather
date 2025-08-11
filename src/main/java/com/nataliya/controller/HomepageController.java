package com.nataliya.controller;

import com.nataliya.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller("/")
@RequiredArgsConstructor
public class HomepageController {

    private static final String INDEX_VIEW = "index";

    @GetMapping
    public String showHomepage(@RequestAttribute("authUserDto") UserDto userDto, Model model) {
        model.addAttribute("userDto", userDto);


        return INDEX_VIEW;
    }
}
