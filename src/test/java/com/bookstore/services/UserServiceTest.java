package com.bookstore.services;

import com.bookstore.converter.UserConverter;
import com.bookstore.dto.UserDto;
import com.bookstore.model.User;
import com.bookstore.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final static Integer ID = 1;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(ID);
        user.setName("test user");
    }

    @Test
    public void testFindByIdSuccess() {
        // given
        var userDto = new UserDto();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userConverter.entityToDto(user)).thenReturn(userDto);

        // when
        var result = userService.findById(ID);

        // then
        verify(userRepository, times(1)).findById(ID);
        verify(userConverter, times(1)).entityToDto(user);
        assertTrue(result.isPresent());
        assertEquals(userDto, result.get());
    }

    @Test
    public void testFindByIdNotFound() {
        // given
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        // when
        verify(userRepository, times(0)).findById(2);
        var result = userService.findById(2);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSaveUserSuccess() {
        // given
        var userDto = new UserDto();
        when(userConverter.dtoToEntity(userDto)).thenReturn(user);
        var createdUser = new User();
        when(userRepository.save(user)).thenReturn(createdUser);
        var createdUserDto = new UserDto();
        when(userConverter.entityToDto(createdUser)).thenReturn(createdUserDto);

        // when
        var result = userService.saveUser(userDto);

        // then
        verify(userConverter).dtoToEntity(userDto);
        verify(userRepository, times(1)).save(user);
        verify(userConverter).entityToDto(createdUser);
        assertEquals(createdUserDto, result);
    }

    @Test
    void testSaveUserWhenUserIsNull() {
        Assertions.assertThrows(RuntimeException.class, () -> userService.saveUser(null));
    }

    @Test
    public void testDeleteById() {
        // when
        userService.deleteById(ID);

        // then
        verify(userRepository, times(1)).deleteById(ID);
    }
}
