package com.vaibhav.icms.user.dto;

import java.util.Set;

import com.vaibhav.icms.user.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {
    
    private String name;

    @Email
    private String email;

    private String password; // <-- optional

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    private Set<Role> roles;

}
