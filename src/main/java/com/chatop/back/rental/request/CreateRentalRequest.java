package com.chatop.back.rental.request;

import lombok.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRentalRequest {
    @NotBlank(message = "Name must not be blank")
    @Size(min = 6, max = 255, message = "Name must be between 6 and 255 characters")
    private String name;

    @NotNull(message = "Surface must not be null")
    @Positive(message = "Surface must be positive")
    private Double surface;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Picture must not be null")
    private MultipartFile picture;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 6, max = 2000, message = "Description must be between 6 and 2000 characters")
    private String description;
}
