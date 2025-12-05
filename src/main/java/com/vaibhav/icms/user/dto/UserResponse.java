package com.vaibhav.icms.user.dto;

import java.util.Set;

import com.vaibhav.icms.user.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    // This DTO is returned whenever user information is sent to the client.

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Set<Role> roles;

    
}
