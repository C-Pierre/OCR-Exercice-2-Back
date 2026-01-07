package com.chatop.back.user.controller;

import lombok.RequiredArgsConstructor;
import com.chatop.back.user.dto.UserDto;
import com.chatop.back.user.dto.UsersDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatop.back.user.service.GetUserService;
import com.chatop.back.user.service.GetUsersService;
import com.chatop.back.user.service.UpdateUserService;
import com.chatop.back.user.service.DeleteUserService;
import com.chatop.back.user.request.UpdateUserRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("${app.basePath}/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for users")
public class UserController {
    private final GetUsersService getUsersService;
    private final GetUserService getUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<UsersDto> getUsers() {
        return ResponseEntity.ok(getUsersService.execute());
    }

    @Operation(summary = "Get user by ID", description = "Returns a single user by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(getUserService.execute(id));
    }

    @Operation(summary = "Update user", description = "Updates the details of an existing user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable Long id,
        @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(updateUserService.execute(id, request));
    }

    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        deleteUserService.execute(id);
        return ResponseEntity.noContent().build();
    }
}
