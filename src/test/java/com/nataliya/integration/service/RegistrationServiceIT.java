package com.nataliya.integration.service;

import com.nataliya.config.ApplicationConfig;
import com.nataliya.config.TestDataSourceConfig;
import com.nataliya.config.TestFlywayConfig;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import com.nataliya.service.RegistrationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class})
@ActiveProfiles("test")
@Transactional
public class RegistrationServiceIT {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "TestName1, Password1",
            "TestName2, Password2",
            "TestName3, Password3"
    })
    void registerUser_withValidData_createsUser(String login, String password) {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(login, password, password);
        registrationService.registerUser(userRegistrationDto);

        Optional<User> actualResult = userRepository.findByLogin(login);
        assertTrue(actualResult.isPresent());
        assertNotNull(actualResult.get().getId());
        assertEquals(login, actualResult.get().getLogin());

    }
}
