package com.chatop.back.user.service;

import com.chatop.back.user.dto.UsersDto;
import com.chatop.back.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.user.repository.port.UserRepositoryPort;

@Service
public class GetUsersService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    public GetUsersService(
        UserRepositoryPort userRepositoryPort,
        UserMapper mapper
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.mapper = mapper;
    }

    @Cacheable("usersCache")
    public UsersDto execute() {
        var users = userRepositoryPort.findAll()
            .stream()
            .map(mapper::toDto)
            .toList();

        return new UsersDto(users);
    }
}
