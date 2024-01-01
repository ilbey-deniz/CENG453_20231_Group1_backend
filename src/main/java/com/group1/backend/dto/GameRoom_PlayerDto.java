package com.group1.backend.dto;

import lombok.Data;

@Data
public class GameRoom_PlayerDto {
    String roomCode;
    PlayerDto player;
}
