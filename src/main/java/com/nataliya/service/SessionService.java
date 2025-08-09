package com.nataliya.service;

import com.nataliya.model.Session;
import com.nataliya.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    public Session createSession(User user, Duration sessionTimeout) {
        return new Session(UUID.randomUUID(), user,
                LocalDateTime.now().plus(sessionTimeout));
    }
}
