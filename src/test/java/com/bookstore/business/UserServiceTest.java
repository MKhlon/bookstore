package com.bookstore.business;

import com.bookstore.model.User;
import com.bookstore.model.repositories.UserRepository;
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
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        // when
        var result = userService.findById(ID);

        // then
        verify(userRepository, times(1)).findById(ID);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
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
        when(userRepository.save(user)).thenReturn(user);

        // when
        var result = userService.saveUser(user);

        // then
        verify(userRepository, times(1)).save(user);
        assertEquals(user, result);
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
