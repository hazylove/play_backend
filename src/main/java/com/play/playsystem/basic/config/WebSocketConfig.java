package com.play.playsystem.basic.config;

import com.play.playsystem.basic.handler.ChatWebSocketHandler;
import com.play.playsystem.basic.handler.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Autowired
    ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler, "/notifications").setAllowedOrigins("*");
        registry.addHandler(chatWebSocketHandler, "/chat").setAllowedOrigins("*");
    }
}
