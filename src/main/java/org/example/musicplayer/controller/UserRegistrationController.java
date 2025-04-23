package org.example.musicplayer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.service.KeycloakService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationController {

    private final KeycloakService keycloakService;

    /**
     * Public endpoint for registering a new user
     * 
     * @param request Registration request
     * @return User ID and status
     */
    @PostMapping("/public/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            String userId = keycloakService.createUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPassword(),
                    Collections.singletonList("USER")
            );
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to create user"));
            }
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("userId", userId, "status", "User created successfully"));
            
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
    
    /**
     * Admin endpoint for registering a new artist
     * 
     * @param request Registration request
     * @return User ID and status
     */
    @PostMapping("/admin/register-artist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> registerArtist(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            String userId = keycloakService.createUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPassword(),
                    List.of("USER", "ARTIST")
            );
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to create artist"));
            }
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("userId", userId, "status", "Artist created successfully"));
            
        } catch (Exception e) {
            log.error("Error creating artist: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
    
    /**
     * User registration request
     */
    public static class UserRegistrationRequest {
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
} 