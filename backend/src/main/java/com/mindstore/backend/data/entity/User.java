package com.mindstore.backend.data.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * User entity
 */
@Table(name = "users")
@Entity
public class User implements UserDetails {

    /**
     * default constructor
     */
    public User(){}

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    /**
     * full name
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * email
     */
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    /**
     * password
     */
    @Column(nullable = false)
    private String password;

    /**
     * created at date
     */
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    /**
     * updated at date
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * is oauth user boolean
     */
    @Column(name = "oauth_user", nullable = true)
    private Boolean isOauthUser = false;

    /**
     * default getter
     * @return List of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * default getter
     * @return password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * default getter
     * @return username string
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * default getter
     * @return boolean if account is expired or not
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * default getter
     * @return boolean if account is locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * default getter
     * @return boolean if credentials are expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * default getter
     * @return boolean if enabled or not
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * default getter
     * @return boolean if user was registered as oauth user
     */
    public Boolean isOauthUser (){
        return this.isOauthUser;
    }

    /**
     * default setter
     * @param isOauthUser boolean if user was registered as oauth user
     */
    public void setIsOauthUser(Boolean isOauthUser){
        this.isOauthUser = isOauthUser;
    }

    /**
     * default setter
     * @param fullName for user
     * @return this context
     */
    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * default getter
     * @return fullname string
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * default setter
     * @param email string
     * @return this context
     */
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * default getter
     * @return email string
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * default setter
     * @param password for user
     * @return this context
     */
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * default getter
     * @return long id
     */
    public Long getId (){
        return this.id;
    }

    /**
     * default setter
     * @param id long
     */
    public void setId(Long id){
        this.id = id;
    }



}