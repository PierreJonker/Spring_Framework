package com.jonkersvault.service;

import com.jonkersvault.model.User;
import com.jonkersvault.repository.UserRepository;
import com.jonkersvault.dto.UserResetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Correctly implement the method here
    public User updateUserDetails(String currentEmail, UserResetRequest userResetRequest) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user's details if provided
        if (userResetRequest.getEmail() != null) {
            user.setEmail(userResetRequest.getEmail());
        }
        if (userResetRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userResetRequest.getPassword()));
        }
        if (userResetRequest.getBirthDate() != null) {
            user.setBirthDate(userResetRequest.getBirthDate());
        }

        return userRepository.save(user); // Save updated user
    }
}
