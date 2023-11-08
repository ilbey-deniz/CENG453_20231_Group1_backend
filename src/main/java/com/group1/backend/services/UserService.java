package com.group1.backend.services;

import com.group1.backend.entities.UserEntity;
import com.group1.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public boolean IsUserExistByName(String userName){
        return this.userRepository.existsByName(userName);
    }

    public String getRoleByName(String userName){
        Optional<UserEntity> user = this.userRepository.findByName(userName);
        return user.orElseThrow().getRole();
    }

    public void saveRepository(UserEntity user){
        this.userRepository.save(user);
    }

}
