package com.vaibhav.icms.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaibhav.icms.auth.dto.LoginRequest;
import com.vaibhav.icms.auth.dto.LoginResponse;
import com.vaibhav.icms.auth.dto.RegisterRequest;
import com.vaibhav.icms.auth.service.AuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){ // @RequestBody LoginRequwst - Spring automatically converts JSON → Java object
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    //REGISTER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")  // Access restricted in SecurityConfig, not here
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request){
        LoginResponse response = authService.register(request);  
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully.");
    }
    
    



    
}
