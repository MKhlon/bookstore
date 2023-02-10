package com.bookstore.controller;


import com.bookstore.business.UserService;
import com.bookstore.model.User;
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
    public void getUserByIdWhenUserExistsShouldReturnOk() {
        // given
        var user = new User();
        user.setId(ID);
        user.setName(USER_NAME);
        when(userService.findById(ID)).thenReturn(Optional.of(user));

        // when
        var response = userController.getUserById(ID);

        // then
        verify(userService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void getUserByIdWhenUserDoesNotExistShouldReturnNotFound() {
        // given
        when(userService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = userController.getUserById(ID);

        // then
        verify(userService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createUserWithValidInputShouldReturnCreatedUser() {
        // given
        var user = new User();
        // userId is set because valid return from saveUser() should have userId
        when(userService.saveUser(user))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(333);
                    return u;
                });

        // when
        var response = userController.createUser(user);

        // then
        verify(userService, times(1)).saveUser(user);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void createUserWithNullInputShouldReturnBadRequest() {
        // when
        var response = userController.createUser(null);

        // then
        verify(userService, never()).saveUser(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateUserWithValidInputShouldReturnUpdatedUser() {
        // given
        var user = new User();
        user.setId(ID);
        user.setName(USER_NAME);

        var newUser = new User();
        newUser.setName("Updated User name");

        // when
        when(userService.findById(ID)).thenReturn(Optional.of(user));
        when(userService.saveUser(newUser)).thenReturn(newUser);

        // then
        var response = userController.updateUser(ID, newUser);
        verify(userService, times(1)).findById(ID);
        verify(userService, times(1)).saveUser(newUser);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(newUser.getName(), Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void updateUserWithNotExistingUserIdShouldReturnNotFound() {
        // given
        when(userService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = userController.updateUser(ID, new User());

        // then
        verify(userService, times(1)).findById(ID);
        verify(userService, never()).saveUser(any(User.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteUserWhenUserExistsShouldDeleteUser() {
        // given
        var user = new User();
        user.setId(ID);
        when(userService.findById(ID)).thenReturn(Optional.of(user));

        // when
        var response = userController.deleteUser(ID);

        // then
        verify(userService, times(1)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void deleteUserWhenUserDoesNotExistShouldReturnNotFound() {
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
