package com.nataliya.handler;

import com.nataliya.dto.UserDto;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.exception.AuthenticationException;
import com.nataliya.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleException(UserAlreadyExistsException ex,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        log.info("Attempt to create duplicate user");
        UserRegistrationDto registrationDto = new UserRegistrationDto(request.getParameter("login"), "", "");

        redirectAttributes.addFlashAttribute("registrationData", registrationDto);
        redirectAttributes.addFlashAttribute("userAlreadyExistsMessage", ex.getMessage());

        return "redirect:/sign-up";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleException (AuthenticationException ex,
                                   HttpServletRequest request,
                                   RedirectAttributes redirectAttributes) {

        UserDto userDto = new UserDto(request.getParameter("login"), "");

        redirectAttributes.addFlashAttribute("signInData", userDto);
        redirectAttributes.addFlashAttribute("authenticationErrorMessage", ex.getMessage());

        return "redirect:/sign-in";
    }
}
