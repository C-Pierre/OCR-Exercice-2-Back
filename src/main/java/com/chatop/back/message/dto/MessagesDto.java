package com.chatop.back.message.dto;

import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class MessagesDto {
    private List<MessageDto> messages;
}

