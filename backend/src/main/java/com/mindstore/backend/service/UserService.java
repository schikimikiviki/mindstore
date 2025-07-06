package com.mindstore.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById (Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User getRecentUser(String email, LocalDateTime createdAt){
        return userRepository.getUserWithMailAdressAndSpecificCreatedAtDate(email, createdAt);
    }




}
