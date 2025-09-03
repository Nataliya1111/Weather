package com.nataliya.handler;

import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.exception.AuthenticationException;
import com.nataliya.exception.SessionNotFoundException;
import com.nataliya.exception.UserAlreadyExistsException;
import com.nataliya.exception.WeatherApiResponseException;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String SIGN_UP_REDIRECT = "redirect:/sign-up";
    private static final String SIGN_IN_REDIRECT = "redirect:/sign-in";
    private static final String ERROR_REDIRECT = "redirect:/error";

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleException(UserAlreadyExistsException ex,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        log.info("Attempt to create duplicate user");
        UserRegistrationDto registrationDto = new UserRegistrationDto(request.getParameter("login"), "", "");

        redirectAttributes.addFlashAttribute("registrationData", registrationDto);
        redirectAttributes.addFlashAttribute("userAlreadyExistsMessage", ex.getMessage());

        return SIGN_UP_REDIRECT;
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleException(AuthenticationException ex,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

        UserAuthenticationDto userAuthenticationDto = new UserAuthenticationDto(request.getParameter("login"), "");

        redirectAttributes.addFlashAttribute("signInData", userAuthenticationDto);
        redirectAttributes.addFlashAttribute("authenticationErrorMessage", ex.getMessage());

        return SIGN_IN_REDIRECT;
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public String handleException(SessionNotFoundException ex,
                                  HttpServletResponse response,
                                  RedirectAttributes redirectAttributes) {
        log.warn(ex.getMessage());

        CookieUtil.deleteSessionIdCookie(response);

        redirectAttributes.addFlashAttribute("errorPageMessage", "Oops! Your session not found or expired!");
        return ERROR_REDIRECT;
    }

    @ExceptionHandler(WeatherApiResponseException.class)
    public String handleException(WeatherApiResponseException ex,
                                  RedirectAttributes redirectAttributes) {

        HttpStatus status = ex.getStatus();

        log.warn(ex.getMessage(), ex);

        if (status == HttpStatus.BAD_REQUEST) {
            redirectAttributes.addFlashAttribute("errorPageMessage", "Bad request: check your input.");
        } else if (status == HttpStatus.URI_TOO_LONG) {
            redirectAttributes.addFlashAttribute("errorPageMessage", "Location name is too long.");
        } else {
            redirectAttributes.addFlashAttribute("errorPageMessage", String.format("Weather API call failed with status: %d - %s.", status.value(), status.getReasonPhrase()));
        }

        return ERROR_REDIRECT;
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletRequest request) {

        log.error("Unhandled exception at [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage(),
                ex);

        return ERROR_REDIRECT;
    }
}
