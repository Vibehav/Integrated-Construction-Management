package com.vaibhav.icms.user.dto;

import java.util.Set;

import com.vaibhav.icms.user.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    private String password; // <-- optional

    private String phone;

    private Set<Role> roles;

}
