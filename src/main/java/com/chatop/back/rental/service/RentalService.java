package com.chatop.back.rental.service;

import java.util.List;
import java.time.LocalDateTime;
import com.chatop.back.user.entity.User;
import com.chatop.back.rental.entity.Rental;
import org.springframework.stereotype.Service;
import com.chatop.back.user.service.UserService;
import com.chatop.back.user.response.UserResponse;
import org.springframework.cache.annotation.Caching;
import com.chatop.back.user.repository.UserRepository;
import com.chatop.back.rental.response.RentalResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.rental.response.RentalsResponse;
import com.chatop.back.rental.validator.RentalValidator;
import com.chatop.back.rental.request.UpdateRentalRequest;
import com.chatop.back.rental.request.CreateRentalRequest;
import com.chatop.back.rental.repository.RentalRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalPictureService rentalPictureService;
    private final RentalResponseService rentalResponseService;
    private final UserRepository userRepository;
    private final UserService userService;

    public RentalService(
        RentalRepository rentalRepository,
        RentalPictureService rentalPictureService,
        RentalResponseService rentalResponseService,
        UserRepository userRepository,
        UserService userService
    ) {
        this.rentalRepository = rentalRepository;
        this.rentalPictureService = rentalPictureService;
        this.rentalResponseService = rentalResponseService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Cacheable("rentalsCache")
    public RentalsResponse getRentals() {
        List<RentalResponse> list = rentalRepository.findAll()
            .stream()
            .map(rentalResponseService::toResponse)
            .toList();

        return new RentalsResponse(list);
    }

    @Cacheable(value = "rentalCache", key = "#id")
    public RentalResponse getRental(Long id) {
        Rental rental = getRentalById(id);
        return rentalResponseService.toResponse(rental);
    }

    @CacheEvict(value = {"rentalsCache"}, allEntries = true)
    public RentalResponse createRental(CreateRentalRequest request) {
        try {
            UserResponse currentUser = userService.getCurrentUser();
            User user = userRepository.getReferenceById(currentUser.getId());

            String filename = rentalPictureService.savePicture(request.getPicture());

            Rental rental = Rental.builder()
                .name(request.getName())
                .surface(request.getSurface())
                .price(request.getPrice())
                .description(request.getDescription())
                .picture(filename)
                .owner(user)
                .createdAt(LocalDateTime.now())
                .build();

            RentalValidator.validateCreateRequest(rental);

            rentalRepository.save(rental);

            return rentalResponseService.toResponse(rental);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create rental: " + e.getMessage(), e);
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "rentalsCache", allEntries = true),
        @CacheEvict(value = "rentalCache", key = "#id")
    })
    public RentalResponse updateRental(Long id, UpdateRentalRequest request) {
        try {
            Rental rental = getRentalById(id);
            RentalValidator.checkRentalOwnerAction(rental, userService.getCurrentUser(), "update");

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
                rental.setPicture(rentalPictureService.savePicture(request.getPicture()));
                modified = true;
            }

            if (modified) {
                rental.setUpdatedAt(LocalDateTime.now());
                RentalValidator.validateUpdateRequest(rental);

                rentalRepository.save(rental);
            }

            return rentalResponseService.toResponse(rental);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update rental: " + e.getMessage(), e);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "rentalsCache", allEntries = true),
            @CacheEvict(value = "rentalCache", key = "#id")
    })
    public void deleteRental(Long id) {
        try {
            Rental rental = getRentalById(id);
            RentalValidator.checkRentalOwnerAction(rental, userService.getCurrentUser(), "delete");
            rentalRepository.delete(rental);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete rental: " + e.getMessage(), e);
        }
    }

    private Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rental not found"));
    }
}
