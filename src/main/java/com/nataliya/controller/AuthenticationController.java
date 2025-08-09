package com.nataliya.controller;

import com.nataliya.dto.UserDto;
import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.service.AuthenticationService;
import com.nataliya.service.SessionService;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@Controller
@RequestMapping("/sign-in")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String SIGN_IN_VIEW = "sign-in";
    private static final String HOME_REDIRECT = "redirect:/";

    private final AuthenticationService authenticationService;
    private final SessionService sessionService;
    private final Duration sessionTimeout;

    @GetMapping
    public String showAuthenticationPage(@ModelAttribute("signInData") UserDto userDto,
                                         HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (CookieUtil.isSessionIdCookiePresent(cookies)) {
            return HOME_REDIRECT;
        }
        return SIGN_IN_VIEW;
    }

    @PostMapping
    public String signIn(@Valid @ModelAttribute("signInData") UserDto userDto,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authenticationErrorMessage", "Invalid username or password");
            return SIGN_IN_VIEW;
        }

        User user = authenticationService.getByLoginAndPassword(userDto);

        Session session = sessionService.createSession(user, sessionTimeout);
        var cookie = CookieUtil.createSessionIdCookie(session.getId(), (int) sessionTimeout.toSeconds());

        response.addCookie(cookie);

        return HOME_REDIRECT;
    }
}
