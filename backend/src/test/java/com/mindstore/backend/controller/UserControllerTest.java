package com.mindstore.backend.controller;

import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.JwtService;
import com.mindstore.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    /**
     *
     * function: check if adding 2 users results in the correct user number as calling the /users/all endpoint
     * @throws Exception when mockMvc cannot be executed
     */
    @Test
    void shouldReturnUserList() throws Exception {
        User userOne = new User();
        userOne.setEmail("one@test.at");
        userOne.setFullName("one user");
        userOne.setPassword("onepassword");

        User userTwo = new User();
        userTwo.setEmail("two@test.at");
        userTwo.setFullName("two user");
        userTwo.setPassword("twopassword");

        List<User> mockUsers = List.of(userOne, userTwo);

        when(userService.findAll()).thenReturn(mockUsers);

        String response = mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Actual response: " + response);


        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("one@test.at"))
                .andExpect(jsonPath("$[1].username").value("two@test.at"));
    }

    /**
     *
     * function: check if the endpoint "/users/{userId}" successfully deletes users
     * @throws Exception when mockMvc cannot be executed
     */
    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        Long userId = 1L;

        // do nothing (successful deletion)
        doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User  deleted successfully"));
    }

    /**
     *
     * function: check if a imaginary user is NOT found in at /users/{userId}
     * @throws Exception when the user is not found or when mockMvc cannot be executed
     */
    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        Long userId = 999L;

        // throw an exception (user not found)
        doThrow(new RuntimeException("User not found")).when(userService).deleteUserById(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
