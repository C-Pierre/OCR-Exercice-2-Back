package com.chatop.back.message.service;

import com.chatop.back.message.dto.MessageDto;
import com.chatop.back.message.entity.Message;
import org.springframework.stereotype.Service;
import com.chatop.back.message.mapper.MessageMapper;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.message.repository.port.MessageRepositoryPort;
import com.chatop.back.message.validator.MessageAuthorizationValidator;

@Service
public class GetMessageService {

    private final MessageMapper messageMapper;
    private final MessageRepositoryPort messageRepositoryPort;
    private final MessageAuthorizationValidator messageAuthorizationValidator;

    public GetMessageService(
        MessageMapper messageMapper,
        MessageRepositoryPort messageRepositoryPort,
        MessageAuthorizationValidator messageAuthorizationValidator
    ) {
        this.messageMapper = messageMapper;
        this.messageRepositoryPort = messageRepositoryPort;
        this.messageAuthorizationValidator = messageAuthorizationValidator;
    }

    @Cacheable(value = "rentalMessageCache", key = "#id")
    public MessageDto execute(Long id) {
        Message message = messageRepositoryPort.getById(id);
        messageAuthorizationValidator.isAllowed(message, "get");
        return messageMapper.toDto(message);
    }
}
