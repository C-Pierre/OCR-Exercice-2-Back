package com.chatop.back.user.repository.port;

import java.util.List;
import com.chatop.back.user.entity.User;
import com.chatop.back.common.exception.NotFoundException;

public interface UserRepositoryPort {

    User getById(Long id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    void save(User user);

    void delete(User user);

    List<User> findAll();
}
