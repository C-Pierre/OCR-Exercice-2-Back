package com.chatop.back.rental.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import lombok.Builder;
import java.time.LocalDateTime;
import com.chatop.back.message.entity.Message;

@Builder
@Getter
@Setter
public class RentalResponse {
    private Long id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Long owner_id;
    private List<Message> messages;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}

