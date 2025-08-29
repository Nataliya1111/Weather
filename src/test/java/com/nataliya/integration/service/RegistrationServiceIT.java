package com.nataliya.integration.service;

import com.nataliya.config.ApplicationConfig;
import com.nataliya.integration.config.TestDataSourceConfig;
import com.nataliya.integration.config.TestFlywayConfig;
import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.exception.UserAlreadyExistsException;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import com.nataliya.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
            "AnotherUser, Pass123@#$",
            "User3, asD1234"
    })
    void registerUser_whenValidData_thenCreatesUser(String login, String password) {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(login, password, password);
        registrationService.registerUser(userRegistrationDto);

        Optional<User> actualResult = userRepository.findByLogin(login);

        assertThat(actualResult)
                .isPresent()
                .get()
                .satisfies((user) -> {
                    assertThat(user.getId()).isNotNull();
                    assertThat(user.getLogin()).isEqualTo(login);
                });
    }

    @Test
    void registerUser_whenDuplicateLogin_thenThrowsException() {

        String userName = "TestName1";

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(userName, "Password1", "Password1");
        registrationService.registerUser(userRegistrationDto);

        assertThatThrownBy(() -> registrationService.registerUser(new UserRegistrationDto(userName, "Pass123@#$", "Pass123@#$")))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}