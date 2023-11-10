package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
public class TopScoreUserDto {
    private String name;
    private BigDecimal totalScore;
}
