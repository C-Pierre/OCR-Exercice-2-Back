package com.chatop.back.message.service;

import com.chatop.back.message.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.message.repository.port.MessageRepositoryPort;
import com.chatop.back.message.validator.MessageAuthorizationValidator;

@Service
public class DeleteMessageService {

    private final MessageAuthorizationValidator messageAuthorizationValidator;
    private final MessageRepositoryPort messageRepositoryPort;

    public DeleteMessageService(
            MessageAuthorizationValidator messageAuthorizationValidator,
            MessageRepositoryPort messageRepositoryPort
    ) {
        this.messageAuthorizationValidator = messageAuthorizationValidator;
        this.messageRepositoryPort = messageRepositoryPort;
    }

    @Caching(evict = {
        @CacheEvict(value = "rentalMessagesCache", allEntries = true),
        @CacheEvict(value = "rentalMessageCache", key = "#id")
    })
    public void execute(Long id) {
        Message message = messageRepositoryPort.getById(id);
        messageAuthorizationValidator.isAllowed(message, "delete");
        messageRepositoryPort.delete(message);
    }
}

