package com.chatop.back.message.repository.port;

import java.util.List;
import com.chatop.back.message.entity.Message;
import com.chatop.back.common.exception.NotFoundException;

public interface MessageRepositoryPort {

    Message getById(Long id) throws NotFoundException;

    void save(Message message);

    void delete(Message message);

    List<Message> findAll();
}
