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

    /**
     * function: set up a test user
     */
    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(2L);
        sampleUser.setEmail("viki@example.com");
        sampleUser.setFullName("Viki");
    }

    /**
     * function: check if the userService returns the expected number of users after inserting the test user.
     * Also, check if the first result of the user service contains the user email of the test user
     */
    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(sampleUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("viki@example.com", result.get(0).getEmail());
    }

    /**
     * function: check if the test user is saved successfully
     */
    @Test
    void shouldSaveUser() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User savedUser = userService.save(sampleUser);

        assertNotNull(savedUser);
        assertEquals("viki@example.com", savedUser.getEmail());
    }

    /**
     * function: check if the user can by found using findByUsedId().
     * Also, check if the email in the result is equal to the test users email
     */
    @Test
    void shouldFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("viki@example.com", result.get().getEmail());
    }

    /**
     * function: check if user deletion is successfull when using deleteUserById()
     */
    @Test
    void shouldDeleteUserById() {
        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }







}