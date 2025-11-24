package com.vaibhav.icms.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import com.vaibhav.icms.user.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    // This DTO is used when creating a new user.
    // name, email, password are mandatory fields
    // phone and role.
    
    @NotBlank(message = "Naam batado bhai apna")
    private String name;

    @NotBlank(message = "Email toh dena padega")
    @Email(message = "Sahi email daalo bhai")
    private String email;

    @NotBlank(message = "Password ke bina aagey nhi jayega")
    @Size(min = 6, message = "Password kam se kam 6 characters ka hona chahiye")
    private String password;

    private String phone;

    @NotNull(message = "Role specify karna zaroori hai")
    private Role role;


}
