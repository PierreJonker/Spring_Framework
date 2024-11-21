package com.jonkersvault.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Existing code...

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Handle logout logic (e.g., invalidate session or JWT)
        return ResponseEntity.ok("Logged out successfully"); // Successfully logged out
    }

}
