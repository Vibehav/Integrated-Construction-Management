package com.vaibhav.icms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vaibhav.icms.auth.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration

@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    
    
    // Step 1(a+b)  Authentication Provider UserDetailsService
    // this connects Password Encoder and User Detail Service  
    // without this Spring cannot authenticate users
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;  
    }

    
    
    @Bean // will need this when writing Authentication manager's logic
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
        throws Exception{ 
            return config.getAuthenticationManager();
    }


    @Bean // no JWT yet 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http   

            //Disable CSRF protection because JWT-based APIs don’t need it ( JWT do not use cookies and are stateless).
            .csrf(csrf -> csrf.disable()) // 


            // STATELESS meaning server will NOT create or store any session for users.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 


            .authorizeHttpRequests(auth -> auth
                                    .requestMatchers("/auth/**").permitAll()
                                    .requestMatchers("/users/**").permitAll()  
                                    .anyRequest().authenticated() )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            


            return http.build();                                 
    }
    
}

// a. Password Encoder to hash Passwords and later will be using to match passwords
// shifted to PassConfig.java
// @Bean
// public PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();


    // // b. check if the email is present, else throw exception
    // shifted to UserService.java 
    // @Bean 
    // public UserDetailsService userDetailsService(UserRepository userRepository){
    //     return username -> userRepository.findByEmail(username)
    //             .orElseThrow(() -> new RuntimeException("user not found by the provided email"));    
    // } 
// } 