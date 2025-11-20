package com.vaibhav.icms.user.dto;

import com.vaibhav.icms.user.enums.Role;

public class UserResponse {
    // This DTO is returned whenever user information is sent to the client.

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name; 
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
}
