package com.nataliya.service;

import com.nataliya.dto.UserRegistrationDto;
import com.nataliya.mapper.UserMapper;
import com.nataliya.model.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public User registerUser(UserRegistrationDto userRegistrationDto) {

        String encodedPassword = BCrypt.hashpw(userRegistrationDto.password(), BCrypt.gensalt());

        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);
        user.setPassword(encodedPassword);

        return userService.saveUserIfNotExists(user);
    }

}
