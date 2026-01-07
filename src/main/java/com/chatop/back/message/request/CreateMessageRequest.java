package com.chatop.back.message.request;

import lombok.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMessageRequest {

    @NotBlank(message = "Message must not be blank")
    @Size(min = 6, max = 2000, message = "Message must be between 6 and 2000 characters")
    private String message;

    @NotNull(message = "User ID must not be null")
    private Long user_id;

    @NotNull(message = "Rental ID must not be null")
    private Long rental_id;
}
