package com.group1.backend.controllers;

import com.group1.backend.entities.UserEntity;
import com.group1.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/get/users")
    public List<UserEntity> findAllUsers(){
        return userService.getAllUsers();
    }
}
