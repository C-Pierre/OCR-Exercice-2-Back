package com.chatop.back.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import com.chatop.back.user.service.UserService;
import com.chatop.back.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;
import com.chatop.back.user.response.UserResponse;
import com.chatop.back.auth.response.AuthResponse;
import com.chatop.back.auth.request.AuthLoginRequest;
import com.chatop.back.auth.request.AuthRegisterRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
    name = "Authentication",
    description = "Endpoints for user registration, login, and retrieving the current user"
)
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Validation error or email/username already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Get current user", description = "Returns the currently authenticated user's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Current user retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: JWT token missing or invalid")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
