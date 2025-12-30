package com.chatop.back.rental.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRentalRequest {
    private String name;
    private Double surface;
    private Double price;
    private MultipartFile picture;
    private String description;
}