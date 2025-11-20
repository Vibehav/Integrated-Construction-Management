package com.vaibhav.icms.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.vaibhav.icms.user.enums.Role;

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


    // Getters and Setters
    public String getEmail(String email) { // get email
        return email;
    }
    public void setEmail(String email) { // set email
        this.email = email;
    }

    public String getName(String name){  // get name
        return name;
    }
    public void setName(String name){ // set name
        this.name = name;
    }

    public String getPassword(String password){ // get password
        return password;
    }
    public void setPassword(String password){ // set password
        this.password = password;
    }

    public String getPhone(String phone){ // get phone
        return phone;
    }
    public void setPhone(String phone){ // set phone
        this.phone = phone;
    }

    public Role getRole(Role role){    // get role
        return role;
    }
    public void setRole(Role role){  // set role
        this.role = role;
    }
}
