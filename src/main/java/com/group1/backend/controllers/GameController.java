package com.group1.backend.controllers;

import com.group1.backend.config.WebsocketConfig;
import com.group1.backend.dto.GameRoom_PlayerDto;
import com.group1.backend.dto.PlayerDto;
import com.group1.backend.dto.RoomCodeDto;
import com.group1.backend.entities.GameRoom;
import com.group1.backend.enums.PlayerColor;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api")
@Tag(name = "Game Creation API endpoints", description = "Involves game creation and deletion")
@Slf4j
public class GameController {

    private final HashMap<String, GameRoom> gameRooms = new HashMap<>();
    private WebsocketConfig websocketConfig;
    private final HashMap<String, AtomicBoolean> ongoingTrades = new HashMap<>();

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
        gameRoom.setIsStarted(false);
        ongoingTrades.put(roomCode, new AtomicBoolean(false));
        gameRooms.put(roomCode, gameRoom);
        log.info("Game room created with room code: " + roomCode);
        return new ResponseEntity<>(roomCode, HttpStatus.OK);
    }

    @PostMapping("/game/join")
    public ResponseEntity<?> joinGame(@RequestBody GameRoom_PlayerDto gameRoomPlayerDto){
        //TODO: move this to a service
        GameRoom gameRoom = gameRooms.get(gameRoomPlayerDto.getRoomCode());
        //checks for room code validity
        if(gameRoom == null){
            return new ResponseEntity<>("Room code is invalid", HttpStatus.BAD_REQUEST);
        }
        //checks whether player name is duplicate
        if(gameRoom.getPlayers().containsKey(gameRoomPlayerDto.getPlayer().getName())){
            return new ResponseEntity<>("Player already joined the room", HttpStatus.BAD_REQUEST);
        }
        //checks whether room is full
        if(gameRoom.getPlayers().size() == 4){
            return new ResponseEntity<>("Room is full", HttpStatus.BAD_REQUEST);
        }
        //checks game has started or not
        if(gameRoom.getIsStarted()){
            return new ResponseEntity<>("Game has already started", HttpStatus.BAD_REQUEST);
        }
        //gives the player a non duplicate color
        PlayerColor color = null;
        while(color == null){
            color = PlayerColor.getRandomColor();
            for(PlayerDto player : gameRoom.getPlayers().values()){
                if(player.getColor() == color){
                    color = null;
                    break;
                }
            }
        }

        gameRoomPlayerDto.getPlayer().setColor(color);
        gameRoom.getPlayers().put(gameRoomPlayerDto.getPlayer().getName(), gameRoomPlayerDto.getPlayer());
        return new ResponseEntity<>(gameRoom, HttpStatus.OK);
    }
    @PostMapping("/game/start")
    public ResponseEntity<?> startGame(@RequestBody GameRoom_PlayerDto gameRoomPlayerDto){
        //check all players are ready
        for(PlayerDto player : gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().values()){
            if(!player.isReady()){
                return new ResponseEntity<>("All players must be ready", HttpStatus.BAD_REQUEST);
            }
        }
        //check there are at least 2 players
        if(gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().size() < 2){
            return new ResponseEntity<>("There must be at least 2 players", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/game/playerKicked")
    public ResponseEntity<?> kickPlayer(@RequestBody GameRoom_PlayerDto gameRoomPlayerDto){
        //return bad request if player is not in the room
        if(!gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().containsKey(gameRoomPlayerDto.getPlayer().getName())){
            return new ResponseEntity<>("Player is not in the room", HttpStatus.BAD_REQUEST);
        }
        gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().remove(gameRoomPlayerDto.getPlayer().getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/game/playerLeft")
    public ResponseEntity<?> playerLeft(@RequestBody GameRoom_PlayerDto gameRoomPlayerDto){
        //return bad request if player is not in the room
        if(!gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().containsKey(gameRoomPlayerDto.getPlayer().getName())){
            return new ResponseEntity<>("Player is not in the room", HttpStatus.BAD_REQUEST);
        }
        //if the player is the host, delete the room
        if(gameRooms.get(gameRoomPlayerDto.getRoomCode()).getHostName().equals(gameRoomPlayerDto.getPlayer().getName())){
            gameRooms.remove(gameRoomPlayerDto.getRoomCode());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().remove(gameRoomPlayerDto.getPlayer().getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/game/playerReady")
    public ResponseEntity<?> playerReady(@RequestBody GameRoom_PlayerDto gameRoomPlayerDto){
        //return bad request if player is not in the room
        if(!gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().containsKey(gameRoomPlayerDto.getPlayer().getName())){
            return new ResponseEntity<>("Player is not in the room", HttpStatus.BAD_REQUEST);
        }
        gameRooms.get(gameRoomPlayerDto.getRoomCode()).getPlayers().get(gameRoomPlayerDto.getPlayer().getName()).setReady(true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/game/tradeInit")
    public ResponseEntity<?> trade(@RequestBody RoomCodeDto roomCodeDto){
        ongoingTrades.get(roomCodeDto.getRoomCode()).set(true);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/game/tradeAccept")
    public ResponseEntity<?> tradeAccept(@RequestBody RoomCodeDto roomCodeDto){
        if(!ongoingTrades.get(roomCodeDto.getRoomCode()).get()){
            return new ResponseEntity<>("There is no ongoing trade", HttpStatus.BAD_REQUEST);
        }
        ongoingTrades.get(roomCodeDto.getRoomCode()).set(false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
