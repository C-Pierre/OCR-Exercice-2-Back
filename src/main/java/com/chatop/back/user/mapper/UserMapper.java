package com.chatop.back.user.mapper;

import com.chatop.back.user.entity.User;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .created_at(user.getCreatedAt())
            .updated_at(user.getUpdatedAt())
            .build();
    }
}
