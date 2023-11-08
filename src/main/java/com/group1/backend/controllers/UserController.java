package com.group1.backend.controllers;

import com.group1.backend.dto.RegisterCredentialDto;
import com.group1.backend.entities.UserEntity;
import com.group1.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/get/users")
    public List<UserEntity> findAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterCredentialDto registerDto) {
        if (userService.IsUserExistByName(registerDto.getName())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        String role = "ROLE_ADMIN";
        user.setRole(role);

        user.setTotalScore(0);

        userService.saveRepository(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
}
