package com.nataliya.integration.service;

import com.nataliya.config.ApplicationConfig;
import com.nataliya.config.TestDataSourceConfig;
import com.nataliya.config.TestFlywayConfig;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import com.nataliya.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class})
@ActiveProfiles("test")
@Transactional
public class RegistrationServiceIT {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @Test
        //parametrised test
    void registerUser() {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("Nataliya", "1Qazwsx", "1Qazwsx");
        registrationService.registerUser(userRegistrationDto);

        //assertNotNull(actualResult);

        Optional<User> actualResult = userRepository.findByLogin("Nataliya");
        assertTrue(actualResult.isPresent());

        //другие тесты здесь же или отдельно, как именовать


    }
}
