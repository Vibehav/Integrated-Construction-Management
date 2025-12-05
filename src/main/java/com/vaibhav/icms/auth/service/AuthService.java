package com.vaibhav.icms.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.vaibhav.icms.auth.dto.LoginRequest;
import com.vaibhav.icms.auth.dto.LoginResponse;
import com.vaibhav.icms.auth.util.JwtUtil;
import com.vaibhav.icms.user.entity.User;
import com.vaibhav.icms.user.enums.Role;
import com.vaibhav.icms.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //  AUTHENTICATE CREDENTIALS
    // ==================================

    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate( 
            new UsernamePasswordAuthenticationToken (
            request.getEmail(),
            request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("user not found"));

        String token = jwtUtil.generateToken(user);

        List<String> roleNames = new ArrayList<>();
        for(Role role:user.getRoles()){
            roleNames.add(role.name());
        }

        return new LoginResponse(token, roleNames);

    }


    

    
    
}
