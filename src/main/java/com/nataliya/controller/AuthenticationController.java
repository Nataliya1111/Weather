package com.nataliya.controller;

import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.service.AuthenticationService;
import com.nataliya.service.SessionService;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String SESSION_ID_COOKIE = "sessionId";
    private static final String SIGN_IN_VIEW = "sign-in";
    private static final String HOME_REDIRECT = "redirect:/";

    private final AuthenticationService authenticationService;
    private final SessionService sessionService;

    @GetMapping("/sign-in")
    public String showAuthenticationPage(@ModelAttribute("signInData") UserAuthenticationDto userAuthenticationDto,
                                         HttpServletRequest request) {

        if (CookieUtil.isSessionIdCookiePresent(request.getCookies())) {
            return HOME_REDIRECT;
        }
        return SIGN_IN_VIEW;
    }

    @PostMapping("/sign-in")
    public String signIn(@Valid @ModelAttribute("signInData") UserAuthenticationDto userAuthenticationDto,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authenticationErrorMessage", "Invalid username or password");
            return SIGN_IN_VIEW;
        }

        authenticationService.authenticate(userAuthenticationDto, response);

        return HOME_REDIRECT;
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(SESSION_ID_COOKIE) String cookieValue, HttpServletResponse response) {

        UUID sessionIdCookieValue = UUID.fromString(cookieValue);
        sessionService.deleteSession(sessionIdCookieValue);
        CookieUtil.deleteSessionIdCookie(response);

        return HOME_REDIRECT;
    }
}
