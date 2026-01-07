package com.chatop.back.common.authorization.validator;

import java.util.function.Function;
import com.chatop.back.user.dto.UserDto;
import org.springframework.stereotype.Service;
import com.chatop.back.user.service.GetCurrentUserService;
import com.chatop.back.common.exception.ForbiddenException;

@Service
public class AuthorizationValidator {

    private final GetCurrentUserService getCurrentUserService;

    public AuthorizationValidator(
        GetCurrentUserService getCurrentUserService
    ) {
        this.getCurrentUserService = getCurrentUserService;
    }

    /**
     * * Vérifie si l'utilisateur courant peut effectuer l'action sur l'entité.
     *
     * @param entity L'entité cible
     * @param ownerExtractor Fonction qui extrait l'utilisateur propriétaire de l'entité
     * @param action Action à effectuer (optionnel, "edit" par défaut)
     * @param <T> Type de l'entité
     */
    public <T> void isAllowed(T entity, Function<T, UserDto> ownerExtractor, String action, String entityName) {
        UserDto currentUser = getCurrentUserService.execute();
        UserDto owner = ownerExtractor.apply(entity);

        if (owner == null || !owner.getId().equals(currentUser.getId())) {
            if (action == null || action.isBlank()) {
                action = "edit";
            }

            throw new ForbiddenException("You are not allowed to " + action + " this " + entityName);
        }
    }
}
