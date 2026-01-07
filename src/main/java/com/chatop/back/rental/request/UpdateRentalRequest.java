package com.chatop.back.rental.request;

import lombok.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRentalRequest {

    @Size(min = 6, max = 255, message = "Name must be between 6 and 255 characters")
    private String name;

    @Positive(message = "Surface must be positive")
    private Double surface;

    @Positive(message = "Price must be positive")
    private Double price;

    private MultipartFile picture;

    @Size(min = 6, max = 2000, message = "Description must be between 6 and 2000 characters")
    private String description;
}
