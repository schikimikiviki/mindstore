package com.mindstore.backend.data.dto;

/**
 * Dto class for user login
 */
public class LoginUserDto {
    private String email;
    private String password;

    /**
     * default constructor
     */
    public LoginUserDto() {
    }

    /**
     * constructor for login user dto
     *
     * @param email user email adress string
     * @param password user password string
     */
    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
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
}

