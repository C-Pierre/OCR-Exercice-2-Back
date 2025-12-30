package com.chatop.back.user.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class UserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;
}

