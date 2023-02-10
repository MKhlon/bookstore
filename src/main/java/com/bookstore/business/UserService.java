package com.bookstore.business;

import com.bookstore.model.User;
import com.bookstore.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Integer id) {
        return this.userRepository.findById(id);
    }

    public User saveUser(User user) {
        if (user == null) {
            throw new RuntimeException("User can not be null");
        }
        return this.userRepository.save(user);
    }

    public void deleteById(Integer id) {
        this.userRepository.deleteById(id);
    }
}
