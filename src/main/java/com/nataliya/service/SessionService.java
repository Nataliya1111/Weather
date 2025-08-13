package com.nataliya.service;

import com.nataliya.exception.SessionNotFoundException;
import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public Session createSession(User user, Duration sessionTimeout) {
        Session session = new Session(user, LocalDateTime.now().plus(sessionTimeout));
        return sessionRepository.save(session);
    }

    public Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found for existing valid sessionId cookie"));
    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
