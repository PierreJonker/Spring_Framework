package com.jonkersvault.controller;

import com.jonkersvault.model.JwtResponse;  // Import JwtResponse DTO
import com.jonkersvault.dto.LoginRequest;
import com.jonkersvault.dto.SignupRequest;
import com.jonkersvault.dto.UserResetRequest;
import com.jonkersvault.model.User;
import com.jonkersvault.security.JwtUtil;  // JWT utility for generating tokens
import com.jonkersvault.service.UserService; // UserService for user business logic
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil for generating tokens

    @Autowired
    private UserService userService; // Inject UserService to manage user business logic

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder to compare hashed passwords

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));  // Hash the password before saving
        user.setBirthDate(signupRequest.getBirthDate());
        userService.registerUser(user);  // Register the user in the database
        return ResponseEntity.ok("User registered successfully!");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        // Get the user from the database by email
        User user = userService.getUserByEmail(loginRequest.getEmail());

        // Check if the user exists
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponse("Invalid credentials"));
        }

        // Check if the provided password matches the stored hashed password using BCrypt
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        // If password doesn't match, it could be an old password format
        if (!passwordMatches) {
            // Check if the password is hashed using a previous method
            // (if you have any logic to check for an old password format, do that here)
            // For simplicity, I'm assuming here we would detect the previous hash format.

            // If the password is in an old format (for example, MD5 or SHA), rehash it
            if (isOldPasswordFormat(user.getPassword())) {
                // Rehash the old password
                String rehashedPassword = passwordEncoder.encode(loginRequest.getPassword());

                // Save the rehashed password in the database
                user.setPassword(rehashedPassword);
                userService.updateUserDetails(user.getEmail(), user);  // Update user with the new hashed password
            }

            // If the password is valid now, generate the token
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        }

        // If credentials are valid, generate the token
        String token = jwtUtil.generateToken(loginRequest.getEmail());

        // Return the token as part of a JwtResponse
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // Helper method to check for old password format
    private boolean isOldPasswordFormat(String password) {
        // You can check for specific patterns or lengths that match your old password format
        // For example, checking length for MD5 (32 characters) or SHA (40 characters)
        return password.length() != 60; // Assuming BCrypt has a 60-character length
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
        updatedUser.setPassword(passwordEncoder.encode(updatedUserRequest.getPassword()));  // Encrypt the new password before saving
        updatedUser.setBirthDate(updatedUserRequest.getBirthDate());

        // Update user details in the database
        userService.updateUserDetails(currentUserEmail, updatedUser);
        return ResponseEntity.ok("User details updated successfully!");
    }

    // Delete user account endpoint
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserAccount() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName(); // Get the email of the authenticated user
            userService.deleteUserByEmail(email); // Delete the user by email
            return ResponseEntity.ok("User account deleted successfully!");
        }
        return ResponseEntity.status(401).body("Unauthorized"); // Unauthorized if not authenticated
    }
}
