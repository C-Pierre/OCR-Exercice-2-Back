package com.chatop.back.message.response;

import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class MessagesResponse {
    private List<MessageResponse> messages;
}

