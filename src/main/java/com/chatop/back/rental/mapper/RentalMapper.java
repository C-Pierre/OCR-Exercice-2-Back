package com.chatop.back.rental.mapper;

import com.chatop.back.rental.dto.RentalDto;
import com.chatop.back.rental.entity.Rental;
import org.springframework.stereotype.Component;
import com.chatop.back.rental.service.picture.EncodeRentalPictureService;

@Component
public class RentalMapper {

    private final EncodeRentalPictureService encodeRentalPictureService;

    public RentalMapper(
        EncodeRentalPictureService encodeRentalPictureService
    ) {
        this.encodeRentalPictureService = encodeRentalPictureService;
    }

    public RentalDto toDto(Rental rental) {

        String base64Image = encodeRentalPictureService.execute(rental.getPicture());

        return RentalDto.builder()
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