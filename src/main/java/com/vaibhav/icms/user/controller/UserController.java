package com.vaibhav.icms.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaibhav.icms.user.dto.CreateUserRequest;
import com.vaibhav.icms.user.dto.ProfileUpdateRequest;
import com.vaibhav.icms.user.dto.ProfileUpdateResponse;
import com.vaibhav.icms.user.dto.UpdateUserRequest;
import com.vaibhav.icms.user.dto.UserResponse;
import com.vaibhav.icms.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;




@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController { 

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);//200
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user); //200
    }

    @PreAuthorize("hasRole('ADMIN')")    
    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email){
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);//200
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request) {
            UserResponse updateUser = userService.updateUser(id, request);
            return ResponseEntity.ok(updateUser);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); //204
    }
    
    @PutMapping("/updateProfile/{id}")
    public ResponseEntity<ProfileUpdateResponse> updateMyProfile(@PathVariable Long id, @Valid @RequestBody ProfileUpdateRequest request){
        ProfileUpdateResponse response = userService.updateMyProfile(id,request);
        return ResponseEntity.ok(response);
    }
};
