package com.chatop.back.user.validator;

import com.chatop.back.user.entity.User;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Service;
import com.chatop.back.common.authorization.validator.AuthorizationValidator;

@Service
public class UserAuthorizationValidator {

    private final AuthorizationValidator authorizationValidator;

    public UserAuthorizationValidator(AuthorizationValidator authorizationValidator) {
        this.authorizationValidator = authorizationValidator;
    }

    public void isAllowed(User user, String action) {
        authorizationValidator.isAllowed(
            user,
            u -> new UserDto(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt(), u.getUpdatedAt()),
            action,
            "user"
        );
    }
}



