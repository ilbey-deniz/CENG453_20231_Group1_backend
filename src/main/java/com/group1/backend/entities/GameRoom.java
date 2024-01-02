package com.group1.backend.entities;

import com.group1.backend.dto.PlayerDto;
import lombok.Data;

import java.util.HashMap;

@Data
public class GameRoom {
    String roomCode;
    String hostName;
    Boolean isStarted;
    HashMap<String, PlayerDto> players;

}
