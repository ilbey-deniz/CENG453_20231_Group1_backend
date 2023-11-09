package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ScoreDto {
    private String name;
    private int score;
    private String time;
}
