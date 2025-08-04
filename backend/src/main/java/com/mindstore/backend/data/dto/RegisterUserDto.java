package com.mindstore.backend.data.dto;

/**
 * Register User DTO class for user registration
 */
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;

    /**
     * Default constructor
     */
    public RegisterUserDto() {
    }

    /**
     * constructor for register user DTO
     *
     * @param email with user email
     * @param password with user password
     * @param fullName with user full name
     */
    public RegisterUserDto(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    /**
     * default getter
     * @return email string
     */
    public String getEmail() {
        return email;
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
     * @return full name string
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * default setter
     * @param email string
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * default setter
     * @param password string
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * default setter
     * @param fullName string
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
