package com.chatop.back.auth.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import com.chatop.back.user.entity.User;
import org.springframework.stereotype.Service;
import com.chatop.back.auth.security.JwtService;
import com.chatop.back.auth.request.LoginRequest;
import com.chatop.back.auth.response.AuthResponse;
import com.chatop.back.auth.request.RegisterRequest;
import com.chatop.back.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return AuthResponse.builder().token(jwtService.generateToken(user.getEmail())).build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return AuthResponse.builder().token(jwtService.generateToken(user.getEmail())).build();
    }
}
