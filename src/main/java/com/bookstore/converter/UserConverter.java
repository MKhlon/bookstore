package com.bookstore.converter;

import com.bookstore.dto.UserDto;
import com.bookstore.model.User;
import com.bookstore.repositories.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends Converter {
    private final RoleRepository roleRepository;

    public UserConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setRoleId(user.getRole().getId());
        userDto.setRoleName(user.getRole().getName().name());
        return userDto;
    }

    public User dtoToEntity(UserDto userDto) {
        User user = new User();
        var role = roleRepository.findById(userDto.getRoleId()).orElse(null);
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        user.setName(userDto.getUserName());
        user.setRole(role);
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
