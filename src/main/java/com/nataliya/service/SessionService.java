package com.nataliya.service;

import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session createSession(User user, Duration sessionTimeout) {
        Session session = new Session(user, LocalDateTime.now().plus(sessionTimeout));
        return sessionRepository.save(session);
    }
}
