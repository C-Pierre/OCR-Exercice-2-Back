package com.chatop.back.user.service;

import java.util.List;
import java.time.LocalDateTime;
import com.chatop.back.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.chatop.back.user.response.UserResponse;
import com.chatop.back.user.response.UsersResponse;
import com.chatop.back.user.validator.UserValidator;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.user.request.UpdateUserRequest;
import com.chatop.back.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final String errorNotFound = "User not found";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable("usersCache")
    public UsersResponse getUsers() {
        try {
            List<UserResponse> list = userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

            return new UsersResponse(list);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get users : " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "userCache", key = "#id")
    public UserResponse getUser(Long id) {
        try {
            User user = getUserById(id);
            return toResponse(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get user : " + e.getMessage(), e);
        }
    }

    public UserResponse getCurrentUser() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new RuntimeException("No authenticated user found");
            }

            String email = auth.getName();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(errorNotFound));

            return this.toResponse(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get current user : " + e.getMessage(), e);
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "usersCache", allEntries = true),
        @CacheEvict(value = "userCache", key = "#id")
    })
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        try {
            User user = getUserById(id);
            UserValidator.validateUserAction(user, getCurrentUser(), "update");

            if (request.getName() != null) user.setName(request.getName());
            if (request.getEmail() != null) user.setEmail(request.getEmail());

            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return toResponse(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update user : " + e.getMessage(), e);
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "usersCache", allEntries = true),
        @CacheEvict(value = "userCache", key = "#id")
    })
    public void deleteUser(Long id) {
        try {
            User user = getUserById(id);
            UserValidator.validateUserAction(user, getCurrentUser(), "delete");

            userRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete user : " + e.getMessage(), e);
        }
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(errorNotFound));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .created_at(user.getCreatedAt())
            .updated_at(user.getUpdatedAt())
            .build();
    }
}