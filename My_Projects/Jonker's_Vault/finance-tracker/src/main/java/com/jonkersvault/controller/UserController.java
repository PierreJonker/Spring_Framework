package com.jonkersvault.controller;

import com.jonkersvault.dto.SignupRequest;
import com.jonkersvault.dto.LoginRequest;
import com.jonkersvault.dto.UserResetRequest; // Import the correct DTO for the update
import com.jonkersvault.model.User;
import com.jonkersvault.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setBirthDate(signupRequest.getBirthDate());
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("Login successful!");
    }

    // Update User details endpoint
    @PutMapping("/update")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserResetRequest updatedUserRequest) {
        // Get the current authenticated user
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Pass only the relevant fields to the service method
        User updatedUser = new User();
        updatedUser.setEmail(updatedUserRequest.getEmail());
        updatedUser.setPassword(updatedUserRequest.getPassword());
        updatedUser.setBirthDate(updatedUserRequest.getBirthDate());

        userService.updateUserDetails(currentUserEmail, updatedUser);
        return ResponseEntity.ok("User details updated successfully!");
    }

    // Delete User account endpoint
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        // Get the current authenticated user
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUserByEmail(currentUserEmail);
        return ResponseEntity.ok("User account deleted successfully!");
    }
}
