package com.mindstore.backend.data;

public class LoginUserDto {
    private String email;
    private String password;


    public LoginUserDto() {
    }


    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

