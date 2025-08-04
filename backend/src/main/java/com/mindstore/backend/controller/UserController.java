package com.mindstore.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.UserService;

/**
 * REST controller for user actions
 */
@RestController
@RequestMapping("/users")
public class UserController {


    private UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    /**
     * user controller
     * @param userService for managing users
     * @param passwordEncoder for encoding user passwords
     * @param userDetailsService spring user details
     */
    public UserController(UserService userService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    /**
     * function: return all users available, usually this will be only 1-2 users
     * note: the initializer creates a test user. This user should always be there.
     * @return a list of users
     */
    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    /**
     * function: get a singular user
     * @param Id of the user that is requested
     * @return Optional User
     */
    @GetMapping("/id/{Id}")
    public Optional<User> getUserById(@PathVariable Long Id) {
        return userService.findUserById(Id);
    }

    /**
     * function: get a user based on their email and the date they were created in the database.
     * For example, you might want to check if user with email text@text.at was created before 2024-07-01T14:30:00
     *
     * @param email the email of the user
     * @param createdAt the time of creation, in ISO time format
     * @return User
     */
    @GetMapping("/recent-users")
    public User getRecentUser(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt) {

        System.out.println("Checking for email=" + email + " created after " + createdAt);

        return userService.getRecentUser(email, createdAt);
    }


    /**
     * function: delete a user that is not needed
     * @param userId the id of the user that is supposed to be deleted
     * @return ok ---> if successfull, else: 401
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "User  deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        }
    }



}
