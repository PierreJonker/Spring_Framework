package com.jonkersvault.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;  // Add this import
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component  // Add this annotation to register it as a Spring Bean
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Respond with 401 Unauthorized when authentication fails
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access: Please login first!");
    }
}
