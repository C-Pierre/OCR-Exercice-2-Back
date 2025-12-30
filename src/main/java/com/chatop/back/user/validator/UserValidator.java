package com.chatop.back.user.validator;

import java.util.regex.Pattern;
import com.chatop.back.user.entity.User;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import com.chatop.back.user.response.UserResponse;

@Service
public class UserValidator {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static void validateCreateRequest(User user) {
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateName(user.getName());
    }

    public static void validateUserAction(User user, UserResponse currentUser, String action) {
        if (action == null || action.isBlank()) {
            action = "edit";
        }

        if (!user.getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to " + action + " this user");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email must not be empty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Email format is invalid");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password must not be empty");
        }

        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
        }

        if (password.length() > 255) {
            throw new ValidationException("Message must be 255 characters maximum");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name must not be empty");
        }

        if (name.length() > 255) {
            throw new ValidationException("Name must be 255 characters maximum");
        }
    }
}
