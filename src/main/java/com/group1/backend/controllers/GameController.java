package com.group1.backend.controllers;

import com.group1.backend.config.WebsocketConfig;
import com.group1.backend.dto.JoinGameDto;
import com.group1.backend.dto.PlayerDto;
import com.group1.backend.entities.GameRoom;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Game Creation API endpoints", description = "Involves game creation and deletion")
public class GameController {

    private final HashMap<String, GameRoom> gameRooms = new HashMap<>();
    WebsocketConfig websocketConfig;
    @PostMapping("/create/game")
    public ResponseEntity<?> createGame(@RequestBody PlayerDto playerDto){
        GameRoom gameRoom = new GameRoom();
        // generate random room code with 5 digits
        String roomCode = String.valueOf((int)(Math.random() * (99999 - 10000 + 1) + 10000));
        List<PlayerDto> players = new ArrayList<PlayerDto>();
        players.add(playerDto);
        gameRoom.setPlayers(players);
        gameRoom.setRoomCode(roomCode);
        gameRooms.put(roomCode, gameRoom);
        return new ResponseEntity<>("Room created success", HttpStatus.OK);
    }

    @PostMapping("/join/game")
    public void joinGame(@RequestBody JoinGameDto joinGameDto){
        GameRoom gameRoom = gameRooms.get(joinGameDto.getRoomCode());
        gameRoom.getPlayers().add(joinGameDto.getPlayer());
        

    }

}
