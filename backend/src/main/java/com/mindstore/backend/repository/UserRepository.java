package com.mindstore.backend.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindstore.backend.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * user repository with extra functions
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * function to find a user by email
     * @param email string
     * @return an optional User
     */
    Optional<User> findByEmail(String email);

    /**
     * function to select users by mail who were created before a certain date
     * @param email string
     * @param createdAt localdatetime
     * @return a user (or none)
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.createdAt > :createdAt")
    User getUserWithMailAdressAndSpecificCreatedAtDate(@Param("email") String email, @Param("createdAt") LocalDateTime createdAt);

}
