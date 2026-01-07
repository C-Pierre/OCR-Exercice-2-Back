package com.chatop.back.rental.service;

import java.util.List;
import com.chatop.back.rental.dto.RentalDto;
import com.chatop.back.rental.dto.RentalsDto;
import org.springframework.stereotype.Service;
import com.chatop.back.rental.mapper.RentalMapper;
import org.springframework.cache.annotation.Cacheable;
import com.chatop.back.rental.repository.RentalRepository;

@Service
public class GetRentalsService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    public GetRentalsService(
            RentalRepository rentalRepository,
            RentalMapper rentalMapper
    ) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
    }

    @Cacheable("rentalsCache")
    public RentalsDto execute() {
        List<RentalDto> list = rentalRepository.findAll()
            .stream()
            .map(rentalMapper::toDto)
            .toList();

        return new RentalsDto(list);
    }
}
