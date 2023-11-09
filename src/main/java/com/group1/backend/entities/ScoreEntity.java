package com.group1.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "score")
public class ScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int scoreId;

    private int score;
    private Date time;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private UserEntity user;
}
