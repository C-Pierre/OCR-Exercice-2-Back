package com.chatop.back.message.repository.adapter;

import java.util.List;
import com.chatop.back.message.entity.Message;
import org.springframework.stereotype.Service;
import com.chatop.back.message.repository.port.MessageRepositoryPort;
import com.chatop.back.common.exception.NotFoundException;
import com.chatop.back.message.repository.MessageRepository;

@Service
public class MessageRepositoryAdapter implements MessageRepositoryPort {

    private final MessageRepository messageRepository;

    public MessageRepositoryAdapter(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message getById(Long id) {
        return messageRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Message not found with id: " + id));
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
