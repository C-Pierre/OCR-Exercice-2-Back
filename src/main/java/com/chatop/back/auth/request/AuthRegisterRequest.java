package com.chatop.back.auth.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegisterRequest {
    private String name;
    private String email;
    private String password;
}