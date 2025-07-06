package com.mindstore.backend.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mindstore.backend.data.dto.UserQueryDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {


    private UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public UserController(UserService userService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/id/{Id}")
    public Optional<User> getUserById(@PathVariable Long Id) {
        return userService.findUserById(Id);
    }

    @GetMapping("/recent-users")
    public User getRecentUser(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt) {

        System.out.println("Checking for email=" + email + " created after " + createdAt);

        return userService.getRecentUser(email, createdAt);
    }



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
