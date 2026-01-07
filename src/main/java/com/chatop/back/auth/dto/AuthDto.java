package com.chatop.back.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AuthDto {
    private String token;
}
