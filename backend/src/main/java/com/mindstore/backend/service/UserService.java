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

    /**
     *
     * function: searches for all users in the user repo
     * @return a list of users available
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }


    /**
     *
     * @param user the user we want to save
     * function: adds a new user to the user repo
     * @return the saved User
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     *
     * @param id - the user id we are searching for
     * function: find a specific user in the user repo
     * @return an Optional User
     */
    public Optional<User> findUserById (Long id) {
        return userRepository.findById(id);
    }

    /**
     *
     * @param id the id of the user we want to delete
     * function: deletes a user from the user repo
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     *
     * @param email string of the user we are looking for
     * @param createdAt LocalDateTime of user creation
     * function: search for a user with email that was created before a specific Date
     * @return User that is found with that query
     */
    public User getRecentUser(String email, LocalDateTime createdAt){
        return userRepository.getUserWithMailAdressAndSpecificCreatedAtDate(email, createdAt);
    }




}
