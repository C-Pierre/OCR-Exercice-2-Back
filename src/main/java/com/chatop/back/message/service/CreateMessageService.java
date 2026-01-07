package com.chatop.back.message.service;

import java.time.LocalDateTime;
import com.chatop.back.user.entity.User;
import com.chatop.back.rental.entity.Rental;
import com.chatop.back.message.entity.Message;
import org.springframework.stereotype.Service;
import com.chatop.back.message.dto.MessageDto;
import com.chatop.back.message.mapper.MessageMapper;
import com.chatop.back.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.rental.repository.RentalRepository;
import com.chatop.back.message.request.CreateMessageRequest;
import com.chatop.back.message.repository.port.MessageRepositoryPort;

@Service
public class CreateMessageService {

    private final MessageMapper messageMapper;
    private final MessageRepositoryPort messageRepositoryPort;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public CreateMessageService(
            MessageMapper messageMapper,
            MessageRepositoryPort messageRepositoryPort,
            RentalRepository rentalRepository,
            UserRepository userRepository
    ) {
        this.messageMapper = messageMapper;
        this.messageRepositoryPort = messageRepositoryPort;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = {"rentalMessagesCache"}, allEntries = true)
    public MessageDto execute(CreateMessageRequest request) {
        User user = userRepository.getReferenceById(request.getUser_id());
        Rental rental = rentalRepository.getReferenceById(request.getRental_id());

        Message message = Message.builder()
            .message(request.getMessage())
            .rental(rental)
            .user(user)
            .createdAt(LocalDateTime.now())
            .build();

        messageRepositoryPort.save(message);

        return messageMapper.toDto(message);
    }
}

