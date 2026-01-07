package com.chatop.back.message.validator;

import com.chatop.back.message.entity.Message;
import com.chatop.back.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import com.chatop.back.common.authorization.validator.AuthorizationValidator;

@Service
public class MessageAuthorizationValidator {

    private final AuthorizationValidator authorizationValidator;
    private final UserMapper userMapper;

    public MessageAuthorizationValidator(
        AuthorizationValidator authorizationValidator,
        UserMapper userMapper
    ) {
        this.authorizationValidator = authorizationValidator;
        this.userMapper = userMapper;
    }

    public void isAllowed(Message message, String action) {
        authorizationValidator.isAllowed(
            message,
            msg -> {
                if (msg.getUser().getId().equals(msg.getRental().getOwner().getId())) {
                    return userMapper.toDto(msg.getUser());
                } else {
                    return userMapper.toDto(msg.getRental().getOwner());
                }
            },
            action,
            "message"
        );
    }
}
