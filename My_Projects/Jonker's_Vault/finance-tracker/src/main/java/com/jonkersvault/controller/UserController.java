package com.jonkersvault.controller;

import com.jonkersvault.dto.LoginRequest;
import com.jonkersvault.dto.SignupRequest;
import com.jonkersvault.dto.UserResetRequest;
import com.jonkersvault.model.JwtResponse;
import com.jonkersvault.model.User;
import com.jonkersvault.security.JwtUtil;
import com.jonkersvault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            User authenticatedUser = userService.authenticateUser(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            String token = jwtUtil.generateToken(authenticatedUser.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse("Invalid credentials"));
        }
    }

    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserResetRequest updatedUserRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userService.getUserByEmail(currentUserEmail);

        existingUser.setEmail(updatedUserRequest.getEmail());
        existingUser.setBirthDate(updatedUserRequest.getBirthDate());

        if (updatedUserRequest.getPassword() != null && !updatedUserRequest.getPassword().isBlank()) {
            existingUser.setPassword(updatedUserRequest.getPassword());
        }

        userService.updateUserDetails(currentUserEmail, existingUser);
        return ResponseEntity.ok("User details updated successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok("User account deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}