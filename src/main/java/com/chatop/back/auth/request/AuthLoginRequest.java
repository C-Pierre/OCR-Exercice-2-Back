package com.chatop.back.auth.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginRequest {
    private String email;
    private String password;
}
