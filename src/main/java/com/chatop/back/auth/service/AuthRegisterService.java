package com.chatop.back.auth.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import com.chatop.back.auth.dto.AuthDto;
import com.chatop.back.user.entity.User;
import com.chatop.back.auth.jwt.JwtService;
import org.springframework.stereotype.Service;
import com.chatop.back.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.auth.request.AuthRegisterRequest;
import com.chatop.back.common.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthRegisterService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @CacheEvict(value = {"usersCache"}, allEntries = true)
    public AuthDto exec(AuthRegisterRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return AuthDto.builder().token(token).build();
    }
}
