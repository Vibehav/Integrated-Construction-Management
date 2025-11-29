package com.vaibhav.icms.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vaibhav.icms.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaibhav.icms.user.dto.CreateUserRequest;

import com.vaibhav.icms.user.dto.UserResponse;
import com.vaibhav.icms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    //to check if email exists, save in db
    private final UserRepository userRepository;
    // encoding password
    private final PasswordEncoder passwordEncoder; // to be written in security config file
    
    public UserResponse createUser(CreateUserRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        //dto to entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save in DB
        User savedUser = userRepository.save(user);

        // Convert Entity → Response DTO
        return mapToResponse(savedUser);
    }

    //return as list 
    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserResponse> responseList = new ArrayList<>();

        for(User user: users){
            UserResponse response = mapToResponse(user);
            responseList.add(response);
        }
        return responseList;
    }

    // public List<UserResponse> getAllUsers() {
    //     return userRepository.findAll()
    //             .stream()
    //             .map(this::mapToResponse)
    //             .collect(Collectors.toList());
    // }

    // Helper: Convert User → UserResponse
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        return response;
    }
    
}   
// roles
//