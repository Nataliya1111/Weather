package com.nataliya.controller;

import com.nataliya.dto.UserDto;
import com.nataliya.model.User;
import com.nataliya.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign-in")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @GetMapping
    public String showAuthorizationPage(@ModelAttribute("signInData") UserDto userDto) {
        return "sign-in";
    }

    @PostMapping
    public String signIn(@Valid @ModelAttribute("signInData") UserDto userDto,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authorizationErrorMessage", "Invalid username or password");
            return "sign-in";
        }

        User user = authorizationService.getByLoginAndPassword(userDto);

        HttpSession session = request.getSession();


        return "redirect:/sign-up";
    }
}
