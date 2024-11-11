package com.jonkersvault.service;

import com.jonkersvault.model.User;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find user by ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Register a new user
    public User registerUser(String email, String password, String birthDate) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // Encrypt password
        newUser.setBirthDate(LocalDate.parse(birthDate));
        return userRepository.save(newUser);
    }

    // Update user details
    public User updateUserDetails(Long id, String email, String password, String birthDate) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email format.");
            }

            User user = userOptional.get();
            user.setEmail(email);
            user.setBirthDate(LocalDate.parse(birthDate));

            if (password != null && !password.isEmpty()) {
                // Only encode the new password if it's provided
                user.setPassword(passwordEncoder.encode(password)); // Encrypt password
            }

            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
    }

    // Delete user by ID
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
