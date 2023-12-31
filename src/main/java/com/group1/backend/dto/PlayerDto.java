package com.group1.backend.dto;


import com.group1.backend.enums.PlayerColor;
import lombok.Data;

@Data
public class PlayerDto {
    String name;
    PlayerColor color;
    boolean ready;
    boolean host;
    boolean cpu;
}
