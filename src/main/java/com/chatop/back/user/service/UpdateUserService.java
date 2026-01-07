package com.chatop.back.user.service;

import java.time.LocalDateTime;
import com.chatop.back.user.entity.User;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Service;
import com.chatop.back.user.mapper.UserMapper;
import org.springframework.cache.annotation.Caching;
import com.chatop.back.user.request.UpdateUserRequest;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.user.repository.port.UserRepositoryPort;
import com.chatop.back.user.validator.UserAuthorizationValidator;

@Service
public class UpdateUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAuthorizationValidator userAuthorizationValidator;
    private final UserMapper mapper;

    public UpdateUserService(
        UserRepositoryPort userRepositoryPort,
        UserAuthorizationValidator userAuthorizationValidator,
        UserMapper mapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.userAuthorizationValidator = userAuthorizationValidator;
        this.mapper = mapper;
    }

    @Caching(evict = {
        @CacheEvict(value = "usersCache", allEntries = true),
        @CacheEvict(value = "userCache", key = "#id")
    })
    public UserDto execute(Long id, UpdateUserRequest request) {
        User user = userRepositoryPort.getById(id);
        userAuthorizationValidator.isAllowed(user, "update");

        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        user.setUpdatedAt(LocalDateTime.now());
        userRepositoryPort.save(user);

        return mapper.toDto(user);
    }
}
