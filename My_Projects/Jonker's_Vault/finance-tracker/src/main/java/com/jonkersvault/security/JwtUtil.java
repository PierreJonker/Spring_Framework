package com.jonkersvault.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // Generate a secure 256-bit key using the Keys utility
    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // This ensures the key is 256 bits

    // Generate JWT token for the user
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Username as subject
                .setIssuedAt(new Date()) // Issued time
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // Expiration time (1 day)
                .signWith(secretKey) // Use the securely generated key
                .compact();
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // Use the securely generated key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate the token
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if the token is valid
    public Boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
