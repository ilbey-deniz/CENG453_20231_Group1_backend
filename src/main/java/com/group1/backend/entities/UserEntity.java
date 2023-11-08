package com.group1.backend.entities;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String name;
    private String email;
    private String password;
    private String role;
    private int totalScore;
    // todo: add private List<Score> scores;  score hold: date + score + placement
}
