package com.group1.backend.controllers;

import com.group1.backend.config.WebsocketConfig;
import com.group1.backend.dto.JoinGameDto;
import com.group1.backend.dto.PlayerDto;
import com.group1.backend.entities.GameRoom;
import com.group1.backend.enums.PlayerColor;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GameController {

    private final HashMap<String, GameRoom> gameRooms = new HashMap<>();
    WebsocketConfig websocketConfig;
    @PostMapping("/game/create")
    public ResponseEntity<?> createGame(@RequestBody PlayerDto playerDto){
        //TODO: move this to a service
        GameRoom gameRoom = new GameRoom();
        // generate random room code with 5 digits
        String roomCode = String.valueOf((int)(Math.random() * (99999 - 10000 + 1) + 10000));
        HashMap<String, PlayerDto> players = new HashMap<>();
        players.put(playerDto.getName(), playerDto);
        gameRoom.setPlayers(players);
        gameRoom.setRoomCode(roomCode);
        gameRoom.setHostName(playerDto.getName());
        gameRooms.put(roomCode, gameRoom);
        log.info("Game room created with room code: " + roomCode);
        return new ResponseEntity<>(roomCode, HttpStatus.OK);
    }

    @PostMapping("/game/join")
    public ResponseEntity<?> joinGame(@RequestBody JoinGameDto joinGameDto){
        //TODO: move this to a service
        GameRoom gameRoom = gameRooms.get(joinGameDto.getRoomCode());
        //checks for room code validity
        if(gameRoom == null){
            return new ResponseEntity<>("Room code is invalid", HttpStatus.BAD_REQUEST);
        }
        //checks whether room is full
        if(gameRoom.getPlayers().size() == 4){
            return new ResponseEntity<>("Room is full", HttpStatus.BAD_REQUEST);
        }
        //gives the player a random color
        //for each color in PlayerColor enum, if the color is not in the game room, assign it to the player
        PlayerColor color = null;
        for (PlayerColor playerColor : PlayerColor.values()) {
            if (!gameRoom.getPlayers().containsValue(playerColor)) {
                color = playerColor;
                break;
            }
        }

        joinGameDto.getPlayer().setColor(color);
        gameRoom.getPlayers().put(joinGameDto.getPlayer().getName(), joinGameDto.getPlayer());
        return new ResponseEntity<>(gameRoom, HttpStatus.OK);
    }

}
