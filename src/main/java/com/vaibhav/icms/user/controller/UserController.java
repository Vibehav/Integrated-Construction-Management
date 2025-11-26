package com.vaibhav.icms.user.controller;

import com.vaibhav.icms.user.dto.*;
import com.vaibhav.icms.user.service.UserService;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController { 

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/getall")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    
};
