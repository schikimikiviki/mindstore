package com.mindstore.backend.data;

/**
 * Login Response Data class, used as return object when user logs in
 */
public class LoginResponse {
    private String token;
    private long expiresIn;

    /**
     * Default constructor
     */
    public LoginResponse() {
    }

    /**
     * Login Response
     *
     * @param token String with jwt token
     * @param expiresIn to show how much time is left until expiration
     */
    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    /**
     * default getter
     * @return token String
     */
    public String getToken() {
        return token;
    }

    /**
     * default getter
     * @return expiresIn long value
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * default setter
     * @param token string
     * @return this context
     */
    public LoginResponse setToken(String token) {
        this.token = token;
        return this; // Return 'this' for method chaining
    }

    /**
     * default setter
     * @param expiresIn long
     * @return this context
     */
    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this; // Return 'this' for method chaining
    }
}
