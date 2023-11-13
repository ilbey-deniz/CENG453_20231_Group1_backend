package com.group1.backend.testServices;

import com.group1.backend.entities.UserEntity;
import com.group1.backend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class TestUserService {

    @Autowired
    UserService userService;

    @Test
    public void testCreateMultipleUser(){
        int firstLength = userService.getAllUsers().size();
        String PASSWORD_PREFIX = "HardPassword";
        String USERNAME_PREFIX = "TestUser";
        String username = userService.getLastTestUserName();
        int lastTestUserNumber = Integer.parseInt(username.substring(USERNAME_PREFIX.length()));
        for(int i = lastTestUserNumber+1; i < lastTestUserNumber+11; i++){
            String name = USERNAME_PREFIX + i;
            String password = PASSWORD_PREFIX + i;
            String email = name + "@mail.com";
            userService.createUser(name, email, password, "ROLE_USER");
        }
        int secondLength = userService.getAllUsers().size();
        Assertions.assertEquals(firstLength + 10, secondLength);
    }

    @Test
    public void testCreateUser(){
        String name = "TestSingleUser";
        String password = "HardPassword";
        String email = name + "TestSingleUser@mail.com";
        UserEntity userToSave = new UserEntity();
        userToSave.setName(name);
        userToSave.setEmail(email);
        userToSave.setPassword(password);
        userToSave.setRole("ROLE_USER");
        userService.createUser(name, email, password, "ROLE_USER");
        Assertions.assertEquals(name, userService.getUserByName(name).orElseThrow().getName());
        Assertions.assertEquals(email, userService.getUserByEmail(email).orElseThrow().getEmail());
        Assertions.assertEquals("ROLE_USER", userService.getUserByName(name).orElseThrow().getRole());
        userService.deleteUserByName(name);
    }

    @Test
    public void testDeleteUser(){
        String lastTestUserName = userService.getLastTestUserName();
        int firstLength = userService.getAllUsers().size();
        userService.deleteUserByName(lastTestUserName);
        int secondLength = userService.getAllUsers().size();
        Assertions.assertEquals(firstLength - 1, secondLength);
    }

    @Test
    public void testSaveUserScore(){
        String username = userService.getLastTestUserName();
        int firstTotalScore = userService.findTotalScoreByName(username);
        userService.saveUserScoreByName(username, 7);
        int secondTotalScore = userService.findTotalScoreByName(username);
        Assertions.assertEquals(firstTotalScore + 7, secondTotalScore);

    }

    @Test
    public void testAddMultipleScoreToOneUser(){
        Random random = new Random();
        List<Integer> digitList = List.of(2,3,4,5,6,7,8);
        int totalChangeInScore = 0;
        String username = userService.getLastTestUserName();
        int firstTotalScore = userService.findTotalScoreByName(username);

        for(int i = 0; i < 10; i++){
            int index = random.nextInt(digitList.size());
            totalChangeInScore += digitList.get(index);
            userService.saveUserScoreByName(username, digitList.get(index));
        }

        int secondTotalScore = userService.findTotalScoreByName(username);
        Assertions.assertEquals(firstTotalScore + totalChangeInScore, secondTotalScore);
    }

    @Test
    public void testAddMultipleScoreToMultipleUser(){
        int totalPlayerToAddScore = 10;
        int totalScoreCountToAddUser = 10;

        Random random = new Random();
        List<Integer> digitList = List.of(2,3,4,5,6,7,8);
        List<String> usernames = userService.findMultipleTestUser(totalPlayerToAddScore);
        for (String name : usernames) {

            int totalChangeInScore = 0;
            int firstTotalScore = userService.findTotalScoreByName(name);

            for(int i = 0; i < totalScoreCountToAddUser; i++){
                int index = random.nextInt(digitList.size());
                totalChangeInScore += digitList.get(index);
                userService.saveUserScoreByName(name, digitList.get(index));
            }

            int secondTotalScore = userService.findTotalScoreByName(name);
            Assertions.assertEquals(firstTotalScore + totalChangeInScore, secondTotalScore);
        }

    }
}
