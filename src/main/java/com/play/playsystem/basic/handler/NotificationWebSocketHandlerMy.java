package com.play.playsystem.basic.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.playsystem.basic.utils.result.MessageResult;
import com.play.playsystem.user.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
public class NotificationWebSocketHandlerMy extends MyAbstractWebSocketHandler {
    @Autowired
    private UserUtil userUtils;

    @Override
    protected Long extractUserIdFromToken(String token) {
        return userUtils.extractUserIdFromToken(token);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 处理传入消息
        log.info("接收消息: {}", message.getPayload());
    }

    public void sendMessageToUser(Long userId, MessageResult messageResult) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(messageResult);
            session.sendMessage(new TextMessage(jsonMessage));
        }
    }
}
