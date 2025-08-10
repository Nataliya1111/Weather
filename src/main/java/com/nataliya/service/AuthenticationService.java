package com.nataliya.service;

import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.exception.AuthenticationException;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public User getByLoginAndPassword(UserAuthenticationDto userAuthenticationDto) {

        User user = userRepository.findByLogin(userAuthenticationDto.login())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!isPasswordMatches(userAuthenticationDto.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return user;
    }

    boolean isPasswordMatches(String plainTextPassword, String encodedPassword) {
        return BCrypt.checkpw(plainTextPassword, encodedPassword);
    }


}
