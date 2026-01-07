package com.chatop.back.user.dto;

import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class UsersDto {
    private List<UserDto> users;
}

