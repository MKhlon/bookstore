package com.bookstore.services;

import com.bookstore.converter.UserConverter;
import com.bookstore.dto.UserDto;
import com.bookstore.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public Optional<UserDto> findById(Integer id) {
        return this.userRepository.findById(id)
                .map(userConverter::entityToDto);
    }

    public UserDto saveUser(UserDto userDto) {
        if (userDto == null) {
            throw new RuntimeException("User can not be null");
        }
        var createdUser = this.userRepository.save(userConverter.dtoToEntity(userDto));
        return userConverter.entityToDto(createdUser);
    }

    public void deleteById(Integer id) {
        this.userRepository.deleteById(id);
    }
}
