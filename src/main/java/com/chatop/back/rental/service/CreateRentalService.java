package com.chatop.back.rental.service;

import java.time.LocalDateTime;

import com.chatop.back.rental.service.picture.EncodeRentalPictureService;
import com.chatop.back.rental.service.picture.SaveRentalPictureService;
import com.chatop.back.user.entity.User;
import com.chatop.back.user.dto.UserDto;
import com.chatop.back.rental.entity.Rental;
import com.chatop.back.rental.dto.RentalDto;
import org.springframework.stereotype.Service;
import com.chatop.back.rental.mapper.RentalMapper;
import org.springframework.cache.annotation.CacheEvict;
import com.chatop.back.rental.request.CreateRentalRequest;
import com.chatop.back.user.service.GetCurrentUserService;
import com.chatop.back.user.repository.port.UserRepositoryPort;
import com.chatop.back.rental.repository.port.RentalRepositoryPort;

@Service
public class CreateRentalService {

    private final RentalRepositoryPort rentalRepositoryPort;
    private final SaveRentalPictureService saveRentalPictureService;
    private final RentalMapper rentalMapper;
    private final UserRepositoryPort userRepositoryPort;
    private final GetCurrentUserService getCurrentUserService;

    public CreateRentalService(
            RentalRepositoryPort rentalRepositoryPort,
            SaveRentalPictureService saveRentalPictureService,
            RentalMapper rentalMapper,
            UserRepositoryPort userRepositoryPort,
            GetCurrentUserService getCurrentUserService
    ) {
        this.rentalRepositoryPort = rentalRepositoryPort;
        this.saveRentalPictureService = saveRentalPictureService;
        this.rentalMapper = rentalMapper;
        this.userRepositoryPort = userRepositoryPort;
        this.getCurrentUserService = getCurrentUserService;
    }

    @CacheEvict(value = {"rentalsCache"}, allEntries = true)
    public RentalDto execute(CreateRentalRequest request) {
        UserDto currentUser = getCurrentUserService.execute();
        User user = userRepositoryPort.getById(currentUser.getId());
        String filename = saveRentalPictureService.execute(request.getPicture());

        Rental rental = Rental.builder()
            .name(request.getName())
            .surface(request.getSurface())
            .price(request.getPrice())
            .description(request.getDescription())
            .picture(filename)
            .owner(user)
            .createdAt(LocalDateTime.now())
            .build();

        rentalRepositoryPort.save(rental);

        return rentalMapper.toDto(rental);
    }
}
