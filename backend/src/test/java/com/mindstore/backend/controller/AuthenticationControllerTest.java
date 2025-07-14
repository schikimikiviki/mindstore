package com.mindstore.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindstore.backend.data.LoginResponse;
import com.mindstore.backend.data.dto.LoginUserDto;
import com.mindstore.backend.data.dto.RegisterUserDto;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.AuthenticationService;
import com.mindstore.backend.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private void mockAuthenticatedUser() {
        Authentication authentication = new TestingAuthenticationToken(
                "testUser",
                "password",
                "ROLE_USER"
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void mockAnonymousUser() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void checkAuth_whenAuthenticated_Ok() throws Exception {
        // Mock JWT token validation
        when(jwtService.extractExpiration(any())).thenReturn(new java.util.Date(System.currentTimeMillis() + 3600000));

        mockAuthenticatedUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/check")
                        .cookie(new Cookie("token", "mockToken")))  // This is part of the request building
                .andExpect(status().isOk())  // Now we chain andExpect() after perform()
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andExpect(jsonPath("$.expiresIn").exists());
    }


    @Test
    void checkAuth_whenNotAuthenticated_Nok() throws Exception {
        mockAnonymousUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/check"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_validCredentials_tokenReturned() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        LoginUserDto loginDto = new LoginUserDto("test@example.com", "password");

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(mockUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("mockToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(cookie().exists("token"));
    }

    @Test
    void signup_validRequest_returnsUser() throws Exception {
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setEmail("new@example.com");
        registerDto.setPassword("password");
        registerDto.setFullName("New User");

        User mockUser = new User();
        mockUser.setEmail("new@example.com");
        mockUser.setFullName("New User");

        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.fullName").value("New User"));
    }
}