package com.chatop.back.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.chatop.back.auth.dto.AuthDto;
import com.chatop.back.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatop.back.auth.service.AuthLoginService;
import com.chatop.back.auth.request.AuthLoginRequest;
import com.chatop.back.auth.service.AuthRegisterService;
import com.chatop.back.auth.request.AuthRegisterRequest;
import com.chatop.back.user.service.GetCurrentUserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("${app.basePath}/auth")
@RequiredArgsConstructor
@Tag(
    name = "Authentication",
    description = "Endpoints for user registration, login, and retrieving the current user"
)
public class AuthController {

    private final AuthLoginService authLoginService;
    private final AuthRegisterService authRegisterService;
    private final GetCurrentUserService getCurrentUserService;

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Validation error or email/username already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(@Valid @RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(authRegisterService.exec(request));
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authLoginService.exec(request));
    }

    @Operation(summary = "Get current user", description = "Returns the currently authenticated user's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Current user retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: JWT token missing or invalid")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(getCurrentUserService.execute());
    }
}
