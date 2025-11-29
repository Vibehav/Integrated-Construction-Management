package com.vaibhav.icms.user.repository;

import org.springframework.stereotype.Repository; // Importing Repository annotation 🔴
import com.vaibhav.icms.user.entity.User;  // Assuming there is a User entity class in this package 🟠

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository; // Importing JpaRepository 🟠


//This interface allows Spring to auto-generate all CRUD queries for User.
@Repository // 🔴
public interface UserRepository extends JpaRepository<User, Long> { // 🟠 🟠

    
    Optional<User> findByEmail(String email); // Method to find a user by email 🟠

    boolean existsByEmail(String email);
}
