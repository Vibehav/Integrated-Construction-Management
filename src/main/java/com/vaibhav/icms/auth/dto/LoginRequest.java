package com.vaibhav.icms.auth.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message="Email cannot be blank.")
    @Column(nullable=false)
    @Email
    private String email;
    
    @NotBlank(message="Password field cannot be blank")
    @Column(nullable=false)
    private String password;
    
}
