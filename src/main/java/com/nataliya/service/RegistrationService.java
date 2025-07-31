package com.nataliya.service;

import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.exception.UserAlreadyExistsException;
import com.nataliya.mapper.UserMapper;
import com.nataliya.model.User;
import com.nataliya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User registerUser(UserRegistrationDto userRegistrationDto) {
        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);
        return saveUserIfNotExists(user);
    }

    private User saveUserIfNotExists(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new UserAlreadyExistsException("User with such login already exists", e);
            } else {
                throw e;
            }
        }
    }
}
