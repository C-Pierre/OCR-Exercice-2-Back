package com.chatop.back.rental.service;

import com.chatop.back.rental.entity.Rental;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.rental.repository.port.RentalRepositoryPort;
import com.chatop.back.rental.validator.RentalAuthorizationValidator;

@Service
public class DeleteRentalService {

    private final RentalRepositoryPort rentalRepositoryPort;
    private final RentalAuthorizationValidator rentalAuthorizationValidator;

    public DeleteRentalService(
        RentalRepositoryPort rentalRepositoryPort,
        RentalAuthorizationValidator rentalAuthorizationValidator
    ) {
        this.rentalRepositoryPort = rentalRepositoryPort;
        this.rentalAuthorizationValidator = rentalAuthorizationValidator;
    }

    @Caching(evict = {
        @CacheEvict(value = "rentalsCache", allEntries = true),
        @CacheEvict(value = "rentalCache", key = "#id")
    })
    public void execute(Long id) {
        Rental rental = rentalRepositoryPort.getById(id);
        rentalAuthorizationValidator.isAllowed(rental, "delete");
        rentalRepositoryPort.delete(rental);
    }
}
