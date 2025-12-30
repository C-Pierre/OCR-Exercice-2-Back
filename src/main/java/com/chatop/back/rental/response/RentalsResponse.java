package com.chatop.back.rental.response;

import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class RentalsResponse {
    private List<RentalResponse> rentals;
}

