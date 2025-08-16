package com.nataliya.service;

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

    private final SessionService sessionService;
    private final Duration sessionTimeout;

    public void authenticate(User user, HttpServletResponse response) {
        Session session = sessionService.createSession(user, sessionTimeout);
        var cookie = CookieUtil.createSessionIdCookie(session.getId().toString(), (int) sessionTimeout.toSeconds());
        response.addCookie(cookie);
    }

}
