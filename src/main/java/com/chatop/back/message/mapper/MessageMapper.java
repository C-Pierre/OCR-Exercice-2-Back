package com.chatop.back.message.mapper;

import com.chatop.back.message.dto.MessageDto;
import com.chatop.back.message.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDto toDto(Message message) {
        return MessageDto.builder()
            .id(message.getId())
            .message(message.getMessage())
            .rental_id(message.getRental().getId())
            .created_at(message.getCreatedAt())
            .build();
    }
}