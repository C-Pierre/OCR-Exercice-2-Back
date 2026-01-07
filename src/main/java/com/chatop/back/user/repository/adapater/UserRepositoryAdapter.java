package com.chatop.back.user.repository.adapater;

import java.util.List;
import com.chatop.back.user.entity.User;
import org.springframework.stereotype.Service;
import com.chatop.back.user.repository.UserRepository;
import com.chatop.back.common.exception.NotFoundException;
import com.chatop.back.user.repository.port.UserRepositoryPort;

@Service
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
