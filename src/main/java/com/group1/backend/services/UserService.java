package com.group1.backend.services;

import com.group1.backend.entities.UserEntity;
import com.group1.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {



    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

}
