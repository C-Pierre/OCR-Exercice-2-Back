package com.chatop.back.message.service;

import java.util.List;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Service;
import com.chatop.back.message.dto.MessageDto;
import com.chatop.back.message.dto.MessagesDto;
import com.chatop.back.message.mapper.MessageMapper;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.user.service.GetCurrentUserService;
import com.chatop.back.message.repository.port.MessageRepositoryPort;

@Service
public class GetMessagesService {

    private final MessageRepositoryPort messageRepositoryPort;
    private final GetCurrentUserService getCurrentUserService;
    private final MessageMapper messageMapper;

    public GetMessagesService(
        MessageRepositoryPort messageRepositoryPort,
        GetCurrentUserService getCurrentUserService,
        MessageMapper messageMapper
    ) {
        this.messageRepositoryPort = messageRepositoryPort;
        this.getCurrentUserService = getCurrentUserService;
        this.messageMapper = messageMapper;
    }

    @Cacheable("rentalMessagesCache")
    public MessagesDto execute() {
        UserDto currentUser = getCurrentUserService.execute();

        List<MessageDto> list = messageRepositoryPort.findAll()
            .stream()
            .filter(msg -> msg.getUser().getId().equals(currentUser.getId())
                    || msg.getRental().getOwner().getId().equals(currentUser.getId()))
            .map(messageMapper::toDto)
            .toList();

        return new MessagesDto(list);
    }
}
