package com.vaibhav.icms.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaibhav.icms.auth.dto.LoginRequest;
import com.vaibhav.icms.auth.dto.LoginResponse;
import com.vaibhav.icms.auth.dto.RegisterRequest;
import com.vaibhav.icms.auth.util.JwtUtil;
import com.vaibhav.icms.exception.EmailAlreadyExistsException;
import com.vaibhav.icms.user.entity.User;
import com.vaibhav.icms.user.enums.Role;
import com.vaibhav.icms.user.service.UserService;
import com.vaibhav.icms.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //  AUTHENTICATE CREDENTIALS 
    // Login section 
    // ==================================

    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate( 
            new UsernamePasswordAuthenticationToken (
            request.getEmail(),
            request.getPassword()
            )
        );

        User user = userService.getUserByMail(request.getEmail());

        String token = jwtUtil.generateToken(user);

        List<String> roleNames = new ArrayList<>();
        for(Role role:user.getRoles()){
            roleNames.add(role.name());
        }

        return new LoginResponse(token, roleNames);

    }

    // Register (Admin Only)
    /*
    Step 1: Check if Email Already Exists
    Step 2: Initialize User (make sure roles are set inside the Set)
    Step 3: save user
    Step 4: Create token and convert set to list (LoginResponse accepts list of string only)
    */
    public LoginResponse register(RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email Already Exists.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if(request.getRoles() != null) {
        Set<Role> roles = request.getRoles();
        user.setRoles(roles);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword())); //Encide and set password

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        List<String> rolesToList = new ArrayList<>();
        for(Role r:savedUser.getRoles()){
            rolesToList.add(r.name());
        } 

        return new LoginResponse(token,rolesToList);
        
    }

    public void logout(){
        // stateless log's out from frontend
    }

    
    
}
