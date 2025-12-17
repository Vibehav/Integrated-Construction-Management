package com.vaibhav.icms.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileUpdateRequest {

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")    
    private String phone;

    @Size(min = 6, message = "The Password Should be greater than 6 characters")
    private String password;
}
