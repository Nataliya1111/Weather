package com.nataliya.controller;

import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sign-up")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String showRegistrationPage(@ModelAttribute("registrationData") UserRegistrationDto userRegistrationDto) {
        return "sign-up";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("registrationData") UserRegistrationDto userRegistrationDto,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("passwordsMatch")) {
                model.addAttribute("passwordsMatchError", bindingResult.getFieldError("passwordsMatch").getDefaultMessage());
            }
            return "sign-up";
        }
        registrationService.registerUser(userRegistrationDto);

        return "redirect:/sign-in";
    }
}
