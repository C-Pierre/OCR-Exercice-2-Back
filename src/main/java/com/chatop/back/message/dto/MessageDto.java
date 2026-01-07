package com.chatop.back.message.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class MessageDto {
    private final Long id;
    private final Long rental_id;
    private final String message;
    private final LocalDateTime created_at;
}

