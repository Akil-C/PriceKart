package com.pricekart.dto.auth;

import jakarta.validation.constraints.*;

/**
 * Login request payload.
 */
public class LoginRequest {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String password;

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
