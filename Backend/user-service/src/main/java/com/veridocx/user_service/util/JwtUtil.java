package com.veridocx.user_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:}")
    private String secret;

    private SecretKey getKey() {
        if (secret == null || secret.length() < 32) {
            // Generate a secure 256-bit key automatically
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            System.out.println("⚠ GENERATED TEMP JWT KEY (USE IN DEV ONLY): " +
                    Base64.getEncoder().encodeToString(key.getEncoded()));
            return key;
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private final long validityMs = 1000L * 60 * 60 * 24; // 24 hours

    public String generateToken(String userId, String email, String role) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
    }
}
