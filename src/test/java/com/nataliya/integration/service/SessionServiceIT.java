package com.nataliya.integration.service;

import com.nataliya.config.ApplicationConfig;
import com.nataliya.integration.config.TestDataSourceConfig;
import com.nataliya.integration.config.TestFlywayConfig;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.exception.SessionNotFoundException;
import com.nataliya.model.Session;
import com.nataliya.model.User;
import com.nataliya.service.RegistrationService;
import com.nataliya.service.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class})
@ActiveProfiles("test")
@Transactional
public class SessionServiceIT {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Duration sessionTimeout;

    @Value("${session.cleanup.period}")
    private long sessionCleanupPeriod;

    @Test
    void deleteExpiredSessions() throws InterruptedException {

        User user = registrationService.registerUser(new UserRegistrationDto("TestName1", "Password1", "Password1"));
        Session session = sessionService.createSession(user, sessionTimeout);
        UUID sessionId = session.getId();

        Thread.sleep(sessionTimeout
                .plus(Duration.ofSeconds(sessionCleanupPeriod))
                .plusSeconds(1));

        assertThatThrownBy(() -> sessionService.getValidSession(sessionId)).isInstanceOf(SessionNotFoundException.class);
    }
}

































































































