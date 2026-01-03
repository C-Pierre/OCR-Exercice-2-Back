package com.chatop.back.message.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class MessageResponse {
    private final Long id;
    private final Long rental_id;
    private final String message;
    private final LocalDateTime created_at;
}

