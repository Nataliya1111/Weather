package com.nataliya.service;

import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.exception.AuthenticationException;
import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final Duration sessionTimeout;

    public void authenticate(User user, HttpServletResponse response) {
        Session session = sessionService.createSession(user, sessionTimeout);
        var cookie = CookieUtil.createSessionIdCookie(session.getId().toString(), (int) sessionTimeout.toSeconds());
        response.addCookie(cookie);
    }

    public User getByLoginAndPassword(UserAuthenticationDto userAuthenticationDto) {

        User user = userRepository.findByLogin(userAuthenticationDto.login())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!isPasswordMatches(userAuthenticationDto.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return user;
    }

    private boolean isPasswordMatches(String plainTextPassword, String encodedPassword) {
        return BCrypt.checkpw(plainTextPassword, encodedPassword);
    }


}
