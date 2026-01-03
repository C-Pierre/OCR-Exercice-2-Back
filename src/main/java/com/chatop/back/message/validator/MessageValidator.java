package com.chatop.back.message.validator;

import com.chatop.back.message.entity.Message;
import com.chatop.back.user.entity.User;
import com.chatop.back.user.response.UserResponse;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class MessageValidator {

    public static void validateCreateRequest(Message message) {
        validateMessage(message.getMessage());
    }

    public static void validateUserMessageAction(Message message, UserResponse currentUser, String action) {
        if (action == null || action.isBlank()) {
            action = "edit";
        }

        if (
            !message.getUser().getId().equals(currentUser.getId())
            && !message.getRental().getOwner().getId().equals(currentUser.getId())
        ) {
            throw new RuntimeException("You are not allowed to " + action + " this message");
        }
    }

    private static void validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new ValidationException("Message must not be empty");
        }

        if (message.length() < 5) {
            throw new ValidationException("Message must be at least 5 characters long");
        }

        if (message.length() > 2000) {
            throw new ValidationException("Message must be 2000 characters maximum");
        }
    }
}
