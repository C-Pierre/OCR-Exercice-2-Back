package com.chatop.back.rental.validator;

import com.chatop.back.rental.entity.Rental;
import com.chatop.back.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import com.chatop.back.common.authorization.validator.AuthorizationValidator;

@Service
public class RentalAuthorizationValidator {

    private final AuthorizationValidator authorizationValidator;
    private final UserMapper userMapper;

    public RentalAuthorizationValidator(
        AuthorizationValidator authorizationValidator,
        UserMapper userMapper
    ) {
        this.authorizationValidator = authorizationValidator;
        this.userMapper = userMapper;
    }

    public void isAllowed(Rental rental, String action) {
        authorizationValidator.isAllowed(
            rental,
            r -> userMapper.toDto(r.getOwner()), // conversion User -> UserDto via mapper
            action,
            "rental"
        );
    }
}