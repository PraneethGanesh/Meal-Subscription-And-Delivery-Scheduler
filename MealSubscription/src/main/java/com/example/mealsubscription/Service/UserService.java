package com.example.mealsubscription.Service;

import com.example.mealsubscription.Entity.User;
import com.example.mealsubscription.Exceptions.UserNotFoundException;
import com.example.mealsubscription.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user, long id) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with Id:" + id + " not found"));
        if (user.getName() != null) updatedUser.setName(user.getName());
        if (user.getEmail() != null) updatedUser.setEmail(user.getEmail());
        return userRepository.save(updatedUser);
    }

    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with Id:" + id + " not found"));
        userRepository.delete(user);
    }
}