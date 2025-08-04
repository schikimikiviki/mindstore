package com.mindstore.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mindstore.backend.data.dto.LoginUserDto;
import com.mindstore.backend.data.dto.RegisterUserDto;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.UserRepository;

/**
 * Service class for authentication functions
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    /**
     *
     * Authentication Service class
     *
     * @param userRepository containing user functionalities, eg. for signup
     * @param authenticationManager manages authentication for the user
     * @param passwordEncoder encodes the user password
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *  function: sign up a user
     *
     * @param input RegisterUSerDTO with name and password
     * @return the saved User
     */
    public User signup(RegisterUserDto input) {
        User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    /**
     * function: used to authenticate a user, for example for the login by using authenticationManager
     *
     *  @param input LoginUserDto
     * @return User
     */
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    /**
     *
     * function: search for a user by using the email
     * if the user isn't found, create them.
     *
     * @param email of the user
     * @return the User that was created
     */
    public User findOrCreateUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(email);
                    newUser.setPassword("oauth-user");
                    newUser.setIsOauthUser(true);

                    return userRepository.save(newUser);
                });
    }

}
