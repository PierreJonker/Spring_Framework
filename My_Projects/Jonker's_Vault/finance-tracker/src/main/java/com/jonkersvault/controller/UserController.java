package com.jonkersvault.controller;

import com.jonkersvault.dto.SignupRequest;
import com.jonkersvault.dto.LoginRequest;
import com.jonkersvault.model.User;
import com.jonkersvault.service.UserService;
import com.jonkersvault.dto.UserResetRequest; // Import the reset request DTO
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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setBirthDate(signupRequest.getBirthDate());

        userService.registerUser(user);

        return ResponseEntity.ok("User registered successfully!");
    }

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

    // Method for updating user details
    @PutMapping("/update")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserResetRequest userResetRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // Get the authenticated user's email

        // Correctly call the updateUserDetails method in UserService
        User updatedUser = userService.updateUserDetails(currentUserEmail, userResetRequest);

        return ResponseEntity.ok("User details updated successfully!");
    }
}
