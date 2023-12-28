package com.group1.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.group1.backend.constants.WebSocketConstants.WEBSOCKET_ENDPOINT;

@Slf4j
public class SocketHandler implements WebSocketHandler {

    List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established with: "+ Objects.requireNonNull(session.getRemoteAddress()).getHostName());
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Message received: " + message.getPayload());
        UriTemplate template = new UriTemplate(WEBSOCKET_ENDPOINT);
        String roomCode = template.match(session.getUri().getPath()).get("roomCode");
        log.info("Room code: " + roomCode);
        for (WebSocketSession sess : sessions) {
            String sessRoomCode = template.match(sess.getUri().getPath()).get("roomCode");
            if (sess.isOpen() && !sess.getId().equals(session.getId()) && roomCode.equals(sessRoomCode)){
                log.info("Sending message to: " + sess.getRemoteAddress().getHostName());
                sess.sendMessage(message);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Error occurred at sender " + session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed with: "+ Objects.requireNonNull(session.getRemoteAddress()).getHostName());
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
