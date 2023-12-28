package com.group1.backend.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.group1.backend.constants.WebSocketConstants.WEBSOCKET_ENDPOINT;

@Configuration
@EnableWebSocket
@Getter
@Setter
public class WebsocketConfig implements WebSocketConfigurer {
    SocketHandler handler = new SocketHandler();
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, WEBSOCKET_ENDPOINT).setAllowedOrigins("*");

    }

}
