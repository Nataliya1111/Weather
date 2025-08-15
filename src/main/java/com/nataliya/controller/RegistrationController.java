package com.nataliya.controller;

import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.model.User;
import com.nataliya.service.AuthenticationService;
import com.nataliya.service.RegistrationService;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private static final String SIGN_UP_VIEW = "sign-up";
    private static final String HOME_REDIRECT = "redirect:/";

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public String showRegistrationPage(@ModelAttribute("registrationData") UserRegistrationDto userRegistrationDto,
                                       HttpServletRequest request) {

        if (CookieUtil.isSessionIdCookiePresent(request.getCookies())) {
            return HOME_REDIRECT;
        }
        return SIGN_UP_VIEW;
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("registrationData") UserRegistrationDto userRegistrationDto,
                           BindingResult bindingResult,
                           Model model,
                           HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("passwordsMatch")) {
                model.addAttribute("passwordsMatchError", bindingResult.getFieldError("passwordsMatch").getDefaultMessage());
            }
            return SIGN_UP_VIEW;
        }
        User user = registrationService.registerUser(userRegistrationDto);
        authenticationService.authenticate(user, response);

        return HOME_REDIRECT;
    }
}
