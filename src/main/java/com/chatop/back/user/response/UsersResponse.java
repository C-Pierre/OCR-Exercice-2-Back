package com.chatop.back.user.response;

import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class UsersResponse {
    private List<UserResponse> users;
}

