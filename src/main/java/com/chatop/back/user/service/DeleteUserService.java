package com.chatop.back.user.service;

import com.chatop.back.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.user.repository.port.UserRepositoryPort;
import com.chatop.back.user.validator.UserAuthorizationValidator;

@Service
public class DeleteUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAuthorizationValidator userAuthorizationValidator;

    public DeleteUserService(
        UserRepositoryPort userRepositoryPort,
        UserAuthorizationValidator userAuthorizationValidator
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.userAuthorizationValidator = userAuthorizationValidator;
    }

    @Caching(evict = {
        @CacheEvict(value = "usersCache", allEntries = true),
        @CacheEvict(value = "userCache", key = "#id")
    })
    public void execute(Long id) {
        User user = userRepositoryPort.getById(id);
        userAuthorizationValidator.isAllowed(user, "delete");
        userRepositoryPort.delete(user);
    }
}
