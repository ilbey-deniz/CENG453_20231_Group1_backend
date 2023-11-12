package com.group1.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class TopScoreUserDto {
    private String name;
    private BigDecimal totalScore;
}
