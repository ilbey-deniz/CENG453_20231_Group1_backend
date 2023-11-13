package com.group1.backend.controllers;

import com.group1.backend.dto.*;
import com.group1.backend.entities.UserEntity;
import com.group1.backend.enums.TimeInterval;
import com.group1.backend.services.EmailService;
import com.group1.backend.services.JwtService;
import com.group1.backend.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "User API endpoints", description = "Involves user operations and score fetching")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }


    @GetMapping("/get/users")
    @SecurityRequirement(name = "bearerToken")
    public List<UserEntity> findAllUsers(){
        List<UserEntity> users = userService.getAllUsers();
        users.forEach(user -> user.setPassword("YOU ARE SECURE"));
        return users;
    }

    @GetMapping("/get/topScorers/weekly")
    @SecurityRequirement(name = "bearerToken")
    public List<TopScoreUserDto> getTopScorersWeekly(){
        return userService.findTopScorerDescending(TimeInterval.WEEKLY);
    }


    @GetMapping("/get/topScorers/monthly")
    @SecurityRequirement(name = "bearerToken")
    public List<TopScoreUserDto> getTopScorersMonthly(){
        return userService.findTopScorerDescending(TimeInterval.MONTHLY);
    }


    @GetMapping("/get/topScorers/all")
    @SecurityRequirement(name = "bearerToken")
    public List<TopScoreUserDto> getTopScorersOfAll(){
        return userService.findTopScorerDescending(TimeInterval.ALL);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterCredentialDto registerDto) {
        if (userService.isUserExistByName(registerDto.getName())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userService.createUser(registerDto.getName(), registerDto.getEmail(),
                registerDto.getPassword(), "ROLE_ADMIN");
        userService.saveUser(user);
        var token = jwtService.generateToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);

    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        if (!userService.isUserExistByName(loginDto.getName())) {
            return new ResponseEntity<>("Username is not found!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userService.getUserByName(loginDto.getName()).orElseThrow();
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Password is incorrect!", HttpStatus.UNAUTHORIZED);
        }
        var token = jwtService.generateToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/saveScore")
    @SecurityRequirement(name = "bearerToken")
    public ResponseEntity<String> saveScore(@RequestBody ScoreDto scoreDto){
        if (userService.saveUserScoreByName(scoreDto.getName(), scoreDto.getScore())){
            return new ResponseEntity<>("Score saved success!", HttpStatus.OK);
        }
        return new ResponseEntity<>("User is not found!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/testEmail")
    public ResponseEntity<String> testEmail(){
        String to = "mustafa.ilbey@hotmail.com";
        String subject = "Test Email";
        String text = "This is a test email.";
        emailService.sendEmail(to, subject, text);
        return new ResponseEntity<>("Email sent success!", HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        String email = forgotPasswordDto.getEmail();
        if (!userService.isUserExistByEmail(email)) {
            return new ResponseEntity<>("Email is not found!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userService.getUserByEmail(email).orElseThrow();
        //generates random password with 12 characters
        String newPass = userService.generateRandomPassword(12);

        user.setPassword(passwordEncoder.encode(newPass));
        userService.saveUser(user);

        String to = user.getEmail();
        String subject = "Password Reset";
        String text = "Your new password is: " + newPass;
        emailService.sendEmail(to, subject, text);
        return new ResponseEntity<>("Email sent success!", HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    @SecurityRequirement(name = "bearerToken")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        if (!userService.isUserExistByName(changePasswordDto.getName())) {
            return new ResponseEntity<>("Username is not found!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userService.getUserByName(changePasswordDto.getName()).orElseThrow();
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<>("Password is incorrect!", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userService.saveUser(user);
        return new ResponseEntity<>("Password changed success!", HttpStatus.OK);
    }

}
