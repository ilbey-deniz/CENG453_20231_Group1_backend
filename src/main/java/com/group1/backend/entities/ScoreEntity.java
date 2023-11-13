package com.group1.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "score")
public class ScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scoreId;

    @Column(nullable = false)
    private int score;
    private LocalDate time;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private UserEntity user;
}
