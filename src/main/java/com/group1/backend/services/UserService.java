package com.group1.backend.services;

import com.group1.backend.entities.ScoreEntity;
import com.group1.backend.entities.UserEntity;
import com.group1.backend.repositories.ScoreRepository;
import com.group1.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
//Very important, releases the connection after the transaction is done
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;

    @Autowired
    public UserService(UserRepository userRepository, ScoreRepository scoreRepository) {
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
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

    public void saveUser(UserEntity user){
        this.userRepository.save(user);
    }

    public void saveUserScoreByName(String userName, int score, String time){
        Optional<UserEntity> user = this.userRepository.findByName(userName);

        ScoreEntity scoreEntity = new ScoreEntity();
        scoreEntity.setUser(user.orElseThrow());
        scoreEntity.setScore(score);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate fTime = LocalDate.parse(time, formatter);
        scoreEntity.setTime(fTime);

        scoreEntity.setUser(user.orElseThrow());

        this.scoreRepository.save(scoreEntity);
    }


}
