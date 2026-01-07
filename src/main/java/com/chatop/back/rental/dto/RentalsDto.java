package com.chatop.back.rental.dto;

import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class RentalsDto {
    private List<RentalDto> rentals;
}

