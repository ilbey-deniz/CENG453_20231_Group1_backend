package com.group1.backend.entities;

import com.group1.backend.dto.PlayerDto;
import lombok.Data;

import java.util.List;

@Data
public class GameRoom {
    String roomCode;
    List<PlayerDto> players;

}
