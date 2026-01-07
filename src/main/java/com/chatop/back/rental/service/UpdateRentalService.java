package com.chatop.back.rental.service;

import java.time.LocalDateTime;
import com.chatop.back.rental.entity.Rental;
import com.chatop.back.rental.dto.RentalDto;
import org.springframework.stereotype.Service;
import com.chatop.back.rental.mapper.RentalMapper;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.rental.request.UpdateRentalRequest;
import com.chatop.back.rental.repository.port.RentalRepositoryPort;
import com.chatop.back.rental.validator.RentalAuthorizationValidator;
import com.chatop.back.rental.service.picture.SaveRentalPictureService;

@Service
public class UpdateRentalService {

    private final RentalMapper rentalMapper;
    private final SaveRentalPictureService saveRentalPictureService;
    private final RentalRepositoryPort rentalRepositoryPort;
    private final RentalAuthorizationValidator rentalAuthorizationValidator;

    public UpdateRentalService(
        RentalMapper rentalMapper,
        SaveRentalPictureService saveRentalPictureService,
        RentalRepositoryPort rentalRepositoryPort,
        RentalAuthorizationValidator rentalAuthorizationValidator
        ) {
        this.rentalMapper = rentalMapper;
        this.rentalRepositoryPort = rentalRepositoryPort;
        this.saveRentalPictureService = saveRentalPictureService;
        this.rentalAuthorizationValidator = rentalAuthorizationValidator;
    }

    @Caching(evict = {
        @CacheEvict(value = "rentalsCache", allEntries = true),
        @CacheEvict(value = "rentalCache", key = "#id")
    })
    public RentalDto execute(Long id, UpdateRentalRequest request) {
        Rental rental = rentalRepositoryPort.getById(id);
        rentalAuthorizationValidator.isAllowed(rental, "update");

        boolean modified = false;

        if (request.getName() != null && !request.getName().equals(rental.getName())) {
            rental.setName(request.getName());
            modified = true;
        }

        if (request.getDescription() != null && !request.getDescription().equals(rental.getDescription())) {
            rental.setDescription(request.getDescription());
            modified = true;
        }

        if (request.getSurface() != null && !request.getSurface().equals(rental.getSurface())) {
            rental.setSurface(request.getSurface());
            modified = true;
        }

        if (request.getPrice() != null && !request.getPrice().equals(rental.getPrice())) {
            rental.setPrice(request.getPrice());
            modified = true;
        }

        if (request.getPicture() != null && !request.getPicture().isEmpty()) {
            rental.setPicture(saveRentalPictureService.execute(request.getPicture()));
            modified = true;
        }

        if (modified) {
            rental.setUpdatedAt(LocalDateTime.now());
            rentalRepositoryPort.save(rental);
        }

        return rentalMapper.toDto(rental);
    }
}
