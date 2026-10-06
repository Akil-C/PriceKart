package com.pricekart.dto.auth;

import jakarta.validation.constraints.*;

/**
 * Registration request payload.
 */
public class RegisterRequest {

    @NotBlank
    @Size(max = 100)
    @Email
    private String username; // email

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    // optional phone number
    @Size(max = 20)
    private String phone;

    // role to assign – CUSTOMER, SELLER, ADMIN (default CUSTOMER)
    private String role = "CUSTOMER";

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
