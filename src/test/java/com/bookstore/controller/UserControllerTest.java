package com.bookstore.controller;


import com.bookstore.dto.UserDto;
import com.bookstore.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final static Integer ID = 777;
    private final static String USER_NAME = "Test user name";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void whenGetExistingUserByIdThenReturnOk() {
        // given
        var userDto = new UserDto();
        userDto.setId(ID);
        userDto.setUserName(USER_NAME);
        when(userService.findById(ID)).thenReturn(Optional.of(userDto));

        // when
        var response = userController.getUserById(ID);

        // then
        verify(userService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userDto, response.getBody());
    }

    @Test
    public void whenGetNotExistingUserByIdThenReturnNotFound() {
        // given
        when(userService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = userController.getUserById(ID);

        // then
        verify(userService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenCreateUserWithValidInputThenReturnCreatedUser() {
        // given
        var userDto = new UserDto();
        // userId is set because valid return from saveUser() should have userId
        when(userService.saveUser(userDto))
                .thenAnswer(invocation -> {
                    UserDto u = invocation.getArgument(0);
                    u.setId(333);
                    return u;
                });

        // when
        var response = userController.createUser(userDto);

        // then
        verify(userService, times(1)).saveUser(userDto);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        Assertions.assertEquals(userDto, response.getBody());
    }

    @Test
    public void whenCreateUserWithNullInputThenReturnBadRequest() {
        // when
        var response = userController.createUser(null);

        // then
        verify(userService, never()).saveUser(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenUpdateUserWithValidInputThenReturnUpdatedUser() {
        // given
        var userDto = new UserDto();
        userDto.setId(ID);
        userDto.setUserName(USER_NAME);
        var newUserDto = new UserDto();
        newUserDto.setUserName("Updated User Name");

        // when
        when(userService.findById(ID)).thenReturn(Optional.of(userDto));
        when(userService.saveUser(newUserDto)).thenReturn(newUserDto);

        // then
        var response = userController.updateUser(ID, newUserDto);
        verify(userService, times(1)).findById(ID);
        verify(userService, times(1)).saveUser(newUserDto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(newUserDto.getUserName(), Objects.requireNonNull(response.getBody()).getUserName());
    }

    @Test
    public void whenUpdateUserWithNotExistingUserIdThenReturnNotFound() {
        // given
        when(userService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = userController.updateUser(ID, new UserDto());

        // then
        verify(userService, times(1)).findById(ID);
        verify(userService, never()).saveUser(any(UserDto.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenDeleteExistingUserThenReturnNoContent() {
        // given
        var userDto = new UserDto();
        userDto.setId(ID);
        when(userService.findById(ID)).thenReturn(Optional.of(userDto));

        // when
        var response = userController.deleteUser(ID);

        // then
        verify(userService, times(1)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void whenDeleteNotExistingUserThenReturnNotFound() {
        // given
        when(userService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = userController.deleteUser(ID);

        // then
        verify(userService, times(0)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
