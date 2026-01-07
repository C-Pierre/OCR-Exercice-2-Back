package com.chatop.back.user.service;

import com.chatop.back.user.entity.User;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Service;
import com.chatop.back.user.mapper.UserMapper;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.user.repository.port.UserRepositoryPort;

@Service
public class GetUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    public GetUserService(
        UserRepositoryPort userRepositoryPort,
        UserMapper mapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.mapper = mapper;
    }

    @Cacheable(value = "userCache", key = "#id")
    public UserDto execute(Long id) {
        User user = userRepositoryPort.getById(id);
        return mapper.toDto(user);
    }
}
