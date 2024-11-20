package com.jonkersvault.controller;

import com.jonkersvault.dto.SignupRequest;
import com.jonkersvault.dto.LoginRequest;
import com.jonkersvault.dto.UserResetRequest;
import com.jonkersvault.model.User;
import com.jonkersvault.security.JwtUtil;  // JWT utility for generating tokens
import com.jonkersvault.service.UserService; // UserService for user business logic
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil for generating tokens

    @Autowired
    private UserService userService; // Inject UserService to manage user business logic

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());  // Make sure password is encrypted before saving
        user.setBirthDate(signupRequest.getBirthDate());
        userService.registerUser(user);  // Register the user in the database
        return ResponseEntity.ok("User registered successfully!");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Authentication logic and token generation
        String token = jwtUtil.generateToken(loginRequest.getEmail());  // Generate JWT token
        return ResponseEntity.ok("Bearer " + token);  // Send the JWT token as response
    }

    // Get user details endpoint (authenticated)
    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName(); // Get the email from the authenticated user
            User user = userService.getUserByEmail(email);  // Get user details by email
            return ResponseEntity.ok(user);  // Return user details in response
        }
        return ResponseEntity.status(401).build(); // Unauthorized if not authenticated
    }

    // Update user details endpoint
    @PutMapping("/update")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserResetRequest updatedUserRequest) {
        // Get the current authenticated user
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Set up the user details to update
        User updatedUser = new User();
        updatedUser.setEmail(updatedUserRequest.getEmail());
        updatedUser.setPassword(updatedUserRequest.getPassword());  // Encrypt the new password before saving
        updatedUser.setBirthDate(updatedUserRequest.getBirthDate());

        // Update user details in the database
        userService.updateUserDetails(currentUserEmail, updatedUser);
        return ResponseEntity.ok("User details updated successfully!");
    }
}
