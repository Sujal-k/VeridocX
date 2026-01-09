package com.veridocx.user_service.service;

import com.veridocx.user_service.dto.AuthResponse;
import com.veridocx.user_service.dto.LoginRequest;
import com.veridocx.user_service.dto.RegisterRequest;
import com.veridocx.user_service.entity.User;
import com.veridocx.user_service.repo.UserRepository;
import com.veridocx.user_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        repo.save(user);

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(token);
    }
}
