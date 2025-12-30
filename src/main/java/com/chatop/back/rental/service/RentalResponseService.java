package com.chatop.back.rental.service;

import com.chatop.back.rental.entity.Rental;
import org.springframework.stereotype.Service;
import com.chatop.back.rental.response.RentalResponse;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RentalResponseService {

    private final RentalPictureService rentalPictureService;

    public RentalResponseService(
            RentalPictureService rentalPictureService
    ) {
        this.rentalPictureService = rentalPictureService;
    }

    public RentalResponse toResponse(Rental rental) {

        String base64Image = rentalPictureService.encodePicture(rental.getPicture());

        return RentalResponse.builder()
            .id(rental.getId())
            .name(rental.getName())
            .surface(rental.getSurface())
            .price(rental.getPrice())
            .description(rental.getDescription())
            .owner_id(rental.getOwner().getId())
            .created_at(rental.getCreatedAt())
            .updated_at(rental.getUpdatedAt())
            .picture(base64Image)
            .build();
    }
}
