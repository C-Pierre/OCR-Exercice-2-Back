package com.chatop.back.message.request;

import lombok.*;
import com.chatop.back.rental.entity.Rental;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMessageRequest {
    private String message;
    private Long user_id;
    private Long rental_id;
}