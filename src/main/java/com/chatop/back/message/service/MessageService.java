package com.chatop.back.message.service;

import java.util.List;
import java.time.LocalDateTime;
import com.chatop.back.user.entity.User;
import com.chatop.back.rental.entity.Rental;
import com.chatop.back.message.entity.Message;
import org.springframework.stereotype.Service;
import com.chatop.back.user.service.UserService;
import com.chatop.back.user.response.UserResponse;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.message.response.MessageResponse;
import com.chatop.back.message.response.MessagesResponse;
import com.chatop.back.message.validator.MessageValidator;
import com.chatop.back.rental.repository.RentalRepository;
import com.chatop.back.message.request.CreateMessageRequest;
import com.chatop.back.message.repository.MessageRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public MessageService(
            MessageRepository messageRepository,
            RentalRepository rentalRepository,
            UserRepository userRepository,
            UserService userService
    ) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Cacheable("rentalMessagesCache")
    public MessagesResponse getMessages() {
        try {
            UserResponse currentUser = userService.getCurrentUser();

            List<MessageResponse> list = messageRepository.findAll()
                .stream()
                .filter(msg -> msg.getUser().getId().equals(currentUser.getId())
                        || msg.getRental().getOwner().getId().equals(currentUser.getId()))
                .map(this::toResponse)
                .toList();

            return new MessagesResponse(list);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get messages : " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "rentalMessageCache", key = "#id")
    public MessageResponse getMessage(Long id) {
        try {
            Message message = getMessageById(id);
            MessageValidator.validateUserMessageAction(message, userService.getCurrentUser(), "get");
            return toResponse(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get message : " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = {"rentalMessagesCache"}, allEntries = true)
    public MessageResponse createMessage(CreateMessageRequest request) {
        try {
            User user = userRepository.getReferenceById(request.getUser_id());
            Rental rental = rentalRepository.getReferenceById(request.getRental_id());

            Message message = Message.builder()
                .message(request.getMessage())
                .rental(rental)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

            MessageValidator.validateCreateRequest(message);
            messageRepository.save(message);

            return toResponse(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to create message : " + e.getMessage(), e);
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "rentalMessagesCache", allEntries = true),
        @CacheEvict(value = "rentalMessageCache", key = "#id")
    })
    public void deleteMessage(Long id) {
        try {
            Message message = getMessageById(id);
            MessageValidator.validateUserMessageAction(message, userService.getCurrentUser(), "delete");
            messageRepository.delete(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get delete : " + e.getMessage(), e);
        }
    }

    private Message getMessageById(Long id) {
        return messageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
            .id(message.getId())
            .message(message.getMessage())
            .rental_id(message.getRental().getId())
            .created_at(message.getCreatedAt())
            .build();
    }
}

