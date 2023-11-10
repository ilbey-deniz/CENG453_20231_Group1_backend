package com.group1.backend.controllers;

import com.group1.backend.dto.RegisterCredentialDto;
import com.group1.backend.dto.ScoreDto;
import com.group1.backend.dto.TopScoreUserDto;
import com.group1.backend.entities.UserEntity;
import com.group1.backend.enums.TimeInterval;
import com.group1.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api")
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

    @GetMapping("/get/topScorers/weekly")
    public List<TopScoreUserDto> getTopScorersWeekly(){
        return userService.findTopScorerDescending(TimeInterval.WEEKLY);
    }


    @GetMapping("/get/topScorers/monthly")
    public List<TopScoreUserDto> getTopScorersMonthly(){
        return userService.findTopScorerDescending(TimeInterval.MONTHLY);
    }


    @GetMapping("/get/topScorers/all")
    public List<TopScoreUserDto> getTopScorersOfAll(){
        return userService.findTopScorerDescending(TimeInterval.ALL);
    }

    @PostMapping("/register")
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

        userService.saveUser(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
    /*@GetMapping("/leaderboard/{time}")
    public List<UserEntity> getLeaderboard(@PathVariable String time){
        return userService.getLeaderboard(time);
    }*/
    @PostMapping("/saveScore")
    public ResponseEntity<String> saveScore(@RequestBody ScoreDto scoreDto){
        userService.saveUserScoreByName(scoreDto.getName(), scoreDto.getScore());
        return new ResponseEntity<>("Score saved success!", HttpStatus.OK);
    }
}
