package com.chatop.back.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;
}

