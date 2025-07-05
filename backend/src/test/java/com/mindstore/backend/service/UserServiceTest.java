package com.mindstore.backend.service;

import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(2L);
        sampleUser.setEmail("viki@example.com");
        sampleUser.setFullName("Viki");
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(sampleUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("viki@example.com", result.get(0).getEmail());
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User savedUser = userService.save(sampleUser);

        assertNotNull(savedUser);
        assertEquals("viki@example.com", savedUser.getEmail());
    }

    @Test
    void shouldFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("viki@example.com", result.get().getEmail());
    }

    @Test
    void shouldDeleteUserById() {
        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }







}