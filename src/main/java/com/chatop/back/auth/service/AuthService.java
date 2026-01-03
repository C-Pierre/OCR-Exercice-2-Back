package com.chatop.back.auth.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import com.chatop.back.user.entity.User;
import com.chatop.back.auth.jwt.JwtService;
import org.springframework.stereotype.Service;
import com.chatop.back.auth.request.AuthLoginRequest;
import com.chatop.back.auth.response.AuthResponse;
import com.chatop.back.user.validator.UserValidator;
import com.chatop.back.auth.request.AuthRegisterRequest;
import com.chatop.back.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @CacheEvict(value = {"usersCache"}, allEntries = true)
    public AuthResponse register(AuthRegisterRequest request) {
        try {
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

            UserValidator.validateCreateRequest(user);

            userRepository.save(user);

            String token = jwtService.generateToken(user.getEmail());
            return AuthResponse.builder().token(token).build();
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public AuthResponse login(AuthLoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            String token = jwtService.generateToken(user.getEmail());
            return AuthResponse.builder().token(token).build();

        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
}
