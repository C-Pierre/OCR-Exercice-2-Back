package com.chatop.back.rental.service;

import com.chatop.back.rental.dto.RentalDto;
import com.chatop.back.rental.entity.Rental;
import org.springframework.stereotype.Service;
import com.chatop.back.rental.mapper.RentalMapper;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.rental.repository.port.RentalRepositoryPort;

@Service
public class GetRentalService {

    private final RentalRepositoryPort rentalRepositoryPort;
    private final RentalMapper rentalMapper;

    public GetRentalService(
            RentalRepositoryPort rentalRepositoryPort,
            RentalMapper rentalMapper
    ) {
        this.rentalRepositoryPort = rentalRepositoryPort;
        this.rentalMapper = rentalMapper;
    }

    @Cacheable(value = "rentalCache", key = "#id")
    public RentalDto execute(Long id) {
        Rental rental = rentalRepositoryPort.getById(id);
        return rentalMapper.toDto(rental);
    }
}
