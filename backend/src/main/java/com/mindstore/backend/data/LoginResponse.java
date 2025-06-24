package com.mindstore.backend.data;

public class LoginResponse {
    private String token;
    private long expiresIn;


    public LoginResponse() {
    }


    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }


    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }


    public LoginResponse setToken(String token) {
        this.token = token;
        return this; // Return 'this' for method chaining
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this; // Return 'this' for method chaining
    }
}
