package com.nataliya.service;

import com.nataliya.dto.UserDto;
import com.nataliya.exception.AuthorizationException;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;

    public User getByLoginAndPassword(UserDto userDto) {

        User user = userRepository.findByLogin(userDto.login())
                .orElseThrow(() -> new AuthorizationException("Invalid username or password"));

        if (!isPasswordMatches(userDto.password(), user.getPassword())) {
            throw new AuthorizationException("Invalid username or password");
        }

        return user;
    }

    boolean isPasswordMatches(String plainTextPassword, String encodedPassword) {
        return BCrypt.checkpw(plainTextPassword, encodedPassword);
    }


}
