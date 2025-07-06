package com.mindstore.backend.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindstore.backend.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.createdAt > :createdAt")
    User getUserWithMailAdressAndSpecificCreatedAtDate(@Param("email") String email, @Param("createdAt") LocalDateTime createdAt);

}
