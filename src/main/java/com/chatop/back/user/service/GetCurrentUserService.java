package com.chatop.back.user.service;

import com.chatop.back.user.entity.User;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Service;
import com.chatop.back.user.mapper.UserMapper;
import com.chatop.back.common.exception.ForbiddenException;
import com.chatop.back.user.repository.port.UserRepositoryPort;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class GetCurrentUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    public GetCurrentUserService(
        UserRepositoryPort userRepositoryPort,
        UserMapper mapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.mapper = mapper;
    }

    public UserDto execute() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("No authenticated user");
        }

        User user = userRepositoryPort.getByEmail(auth.getName());

        return mapper.toDto(user);
    }
}
