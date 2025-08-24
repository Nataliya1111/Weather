package com.nataliya.service;

import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final SessionService sessionService;
    private final Duration sessionTimeout;

    public void authenticate(User user, HttpServletResponse response) {
        Session session = sessionService.createSession(user, sessionTimeout);
        var cookie = CookieUtil.createSessionIdCookie(session.getId().toString(), (int) sessionTimeout.toSeconds());
        response.addCookie(cookie);
    }

    public void authenticate(UserAuthenticationDto userAuthenticationDto, HttpServletResponse response) {
        User user = userService.getByLoginAndPassword(userAuthenticationDto);
        authenticate(user, response);
    }

}
