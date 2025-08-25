package com.nataliya.integration.service;

import com.nataliya.config.ApplicationConfig;
import com.nataliya.config.TestDataSourceConfig;
import com.nataliya.config.TestFlywayConfig;
import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.exception.AuthenticationException;
import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.service.AuthenticationService;
import com.nataliya.service.RegistrationService;
import com.nataliya.service.SessionService;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class})
@ActiveProfiles("test")
@Transactional
public class AuthenticationServiceIT {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Duration sessionTimeout;

    @ParameterizedTest
    @CsvSource({
            "TestName1, Password1",
            "AnotherUser, Pass123@#$",
            "User3, asD1234"
    })
    void authenticate_whenRegisteredUser_thenAuthenticationSuccessful(String login, String password) {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(login, password, password);
        User user = registrationService.registerUser(userRegistrationDto);
        MockHttpServletResponse response = new MockHttpServletResponse();

        authenticationService.authenticate(user, response);

        Optional<Cookie> cookieOptional = CookieUtil.findSessionIdCookie(response.getCookies());

        assertThat(cookieOptional).isPresent();

        Cookie sessionIdCookie = cookieOptional.get();
        UUID sessionId = UUID.fromString(sessionIdCookie.getValue());

        Session session = sessionService.getValidSession(sessionId);

        LocalDateTime now = LocalDateTime.now();

        assertAll(
                () -> assertThat(session.getUser()).isEqualTo(user),
                () -> assertThat(session.getExpiresAt())
                        .isAfter(now)
                        .isBeforeOrEqualTo(now.plus(sessionTimeout))
        );
    }

    @Test
    void authenticate_whenUnregisteredUser_thenAuthenticationFails() {

        String login = "TestName1";
        String password = "Password1";

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(login, password, password);
        registrationService.registerUser(userRegistrationDto);

        UserAuthenticationDto userAuthenticationDto1 = new UserAuthenticationDto("AnotherLogin", "AnotherPassword1");
        UserAuthenticationDto userAuthenticationDto2 = new UserAuthenticationDto("AnotherLogin", password);
        UserAuthenticationDto userAuthenticationDto3 = new UserAuthenticationDto(login, "AnotherPassword1");

        MockHttpServletResponse response = new MockHttpServletResponse();

        assertAll(
                () -> assertThatThrownBy(() -> authenticationService.authenticate(userAuthenticationDto1, response))
                        .isInstanceOf(AuthenticationException.class)
                        .hasMessage("Invalid username or password"),
                () -> assertThatThrownBy(() -> authenticationService.authenticate(userAuthenticationDto2, response))
                        .isInstanceOf(AuthenticationException.class)
                        .hasMessage("Invalid username or password"),
                () -> assertThatThrownBy(() -> authenticationService.authenticate(userAuthenticationDto3, response))
                        .isInstanceOf(AuthenticationException.class)
                        .hasMessage("Invalid username or password")
        );
    }
}
