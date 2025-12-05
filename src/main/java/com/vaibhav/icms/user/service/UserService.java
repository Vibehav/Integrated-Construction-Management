package com.vaibhav.icms.user.service;



import java .util.ArrayList;
import java.util.List;


import com.vaibhav.icms.user.entity.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaibhav.icms.user.dto.CreateUserRequest;
import com.vaibhav.icms.user.dto.UpdateUserRequest;
import com.vaibhav.icms.exception.EmailAlreadyExistsException;
import com.vaibhav.icms.user.dto.UserResponse;
import com.vaibhav.icms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserResponse createUser(CreateUserRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        

        //dto to entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRoles(request.getRoles());


       // user.setCreatedAt(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save in DB
        User savedUser = userRepository.save(user);

        // Convert Entity → Response DTO
        return mapToResponse(savedUser);
    }

    public List<UserResponse> getAllUsers(){
    //return as list 
        List<User> users = userRepository.findAll();
        List<UserResponse> responseList = new ArrayList<>();

        for(User user: users){
            UserResponse response = mapToResponse(user);
            responseList.add(response);
        }
        return responseList;
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id:" + id));
        return mapToResponse(user);
    }
    
    public UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(email));
        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request){
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id" + id));
        
        if(!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRoles(request.getRoles());

        //only update if provided
        if(request.getPassword() != null && !request.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updateUser = userRepository.save(user);
        return mapToResponse(updateUser);
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new UsernameNotFoundException("user not found with id:" + id);
        }
        userRepository.deleteById(id);
    }

    @Override // UserDetailsService is a Spring Security interface used to load a user from the database during authentication.
    public UserDetails loadUserByUsername(String email){
        return userRepository.findByEmail(email)
               .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    // Helper: Convert User → UserResponse

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRoles(user.getRoles());
        return response;
    }
    
}   
// roles
//