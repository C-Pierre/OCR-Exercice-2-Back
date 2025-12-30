package com.chatop.back.auth.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AuthResponse {
    private String token;
}
