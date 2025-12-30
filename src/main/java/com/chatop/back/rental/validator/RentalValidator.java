package com.chatop.back.rental.validator;

import com.chatop.back.rental.entity.Rental;
import com.chatop.back.user.response.UserResponse;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class RentalValidator {

    public static void validateCreateRequest(Rental rental) {
        validateName(rental.getName());
        validateSurface(rental.getSurface());
        validatePrice(rental.getPrice());
        validateDescription(rental.getDescription());
        validatePicture(rental.getPicture());
    }

    public static void validateUpdateRequest(Rental rental) {
        validateName(rental.getName());
        validateSurface(rental.getSurface());
        validatePrice(rental.getPrice());
        validateDescription(rental.getDescription());
        validatePicture(rental.getPicture());
    }

    public static void checkRentalOwnerAction(Rental rental, UserResponse currentUser, String action) {
        if (action == null || action.isBlank()) {
            action = "edit";
        }

        if (!rental.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to " + action + " this rental");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Rental name must not be empty");
        }

        if (name.length() > 255) {
            throw new ValidationException("Name must be 255 characters maximum");
        }
    }

    private static void validateSurface(Double surface) {
        if (surface == null || surface <= 0) {
            throw new ValidationException("Surface must be a positive number");
        }
    }

    private static void validatePrice(Double price) {
        if (price == null || price < 0) {
            throw new ValidationException("Price must be zero or positive");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Description must not be empty");
        }

        if (description.length() < 5) {
            throw new ValidationException("Description must be at least 5 characters long");
        }

        if (description.length() > 2000) {
            throw new ValidationException("Description must be 2000 characters maximum");
        }
    }

    private static void validatePicture(String picture) {
        if (picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Picture is required");
        }
    }
}
