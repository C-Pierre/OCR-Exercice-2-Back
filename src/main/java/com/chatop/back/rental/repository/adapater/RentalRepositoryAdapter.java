package com.chatop.back.rental.repository.adapater;

import java.util.List;
import com.chatop.back.rental.entity.Rental;
import org.springframework.stereotype.Service;
import com.chatop.back.common.exception.NotFoundException;
import com.chatop.back.rental.repository.RentalRepository;
import com.chatop.back.rental.repository.port.RentalRepositoryPort;

@Service
public class RentalRepositoryAdapter implements RentalRepositoryPort {

    private final RentalRepository rentalRepository;

    public RentalRepositoryAdapter(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Rental not found with id: " + id));
    }

    @Override
    public void save(Rental rental) {
        rentalRepository.save(rental);
    }

    @Override
    public void delete(Rental rental) {
        rentalRepository.delete(rental);
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
