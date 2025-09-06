package com.nataliya.service;

import com.nataliya.dto.UserAuthenticationDto;
import com.nataliya.exception.AuthenticationException;
import com.nataliya.exception.UserAlreadyExistsException;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUserIfNotExists(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                throw new UserAlreadyExistsException("User with such login already exists", ex);
            } else {
                throw ex;
            }
        }
    }

    public User getByLoginAndPassword(UserAuthenticationDto userAuthenticationDto) {

        User user = userRepository.findByLogin(userAuthenticationDto.login())
                .orElseThrow(() -> new AuthenticationException("Invalid username during authentication"));

        if (!isPasswordMatches(userAuthenticationDto.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid password during authentication");
        }

        return user;
    }

    public User getByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    private boolean isPasswordMatches(String plainTextPassword, String encodedPassword) {
        return BCrypt.checkpw(plainTextPassword, encodedPassword);
    }
}
