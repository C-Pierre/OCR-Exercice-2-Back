package com.chatop.back.rental.repository.port;

import java.util.List;
import com.chatop.back.rental.entity.Rental;
import com.chatop.back.common.exception.NotFoundException;

public interface RentalRepositoryPort {

    Rental getById(Long id) throws NotFoundException;

    void save(Rental rental);

    void delete(Rental rental);

    List<Rental> findAll();
}
