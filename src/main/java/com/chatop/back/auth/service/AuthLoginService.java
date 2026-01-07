package com.chatop.back.auth.service;

import lombok.RequiredArgsConstructor;
import com.chatop.back.auth.dto.AuthDto;
import com.chatop.back.user.entity.User;
import com.chatop.back.auth.jwt.JwtService;
import org.springframework.stereotype.Service;
import com.chatop.back.auth.request.AuthLoginRequest;
import com.chatop.back.user.repository.UserRepository;
import com.chatop.back.common.exception.NotFoundException;
import com.chatop.back.common.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthDto exec(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return AuthDto.builder().token(token).build();
    }
}
