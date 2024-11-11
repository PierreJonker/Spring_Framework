package com.jonkersvault.service;

import com.jonkersvault.model.User;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for dependencies
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user with password hashing
    public User registerUser(User user) {
        // Check if the email already exists in the database
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        // Hash the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Password encryption

        return userRepository.save(user); // Save the user to the database with the hashed password
    }

    // Update user details (email, password, birthdate)
    public void updateUserDetails(String email, User updatedUser) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Only update the allowed fields
        user.setEmail(updatedUser.getEmail());  // Update email
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));  // Hash the updated password
        }
        user.setBirthDate(updatedUser.getBirthDate());  // Update birthdate

        // Save the user
        userRepository.save(user);
    }

    // Delete user account
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user); // Deletes the user from the database
    }
}
