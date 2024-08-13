package com.play.playsystem.basic.handler;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.playsystem.basic.utils.result.MessageResult;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.user.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.play.playsystem.basic.constant.AuthConstant.TOKEN_REDIS_PREFIX;

@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    private final Map<Long, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String token = extractToken(session);
        Long userId = extractUserIdFromToken(token);
        if (userId != null) {
            session.getAttributes().put("userId", userId);
            sessions.put(userId, session);
        } else {
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭WebSocket会话异常: ", e);
            }
        }
    }

    private Long extractUserIdFromToken(String token) {
        if (StrUtil.isBlankOrUndefined(token)) {
            return null;
        }

        Claims claims = jwtUtil.getClaimsByToken(token);
        if (claims == null) {
            log.error("无效token: {}", token);
            return null;
        }

        Long userId = Long.valueOf(claims.getSubject());
        Object storedToken = redisUtil.get(TOKEN_REDIS_PREFIX + userId);
        if (storedToken == null || !storedToken.equals(token)) {
            log.error("用户id {} 的令牌失效: {}", userId, token);
            return null;
        }

        return userId;
    }


    private String extractToken(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return uri.substring(uri.indexOf("token=") + 6);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming messages
    }

    public void sendMessageToUser(Long userId, MessageResult messageResult) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(messageResult);
            session.sendMessage(new TextMessage(jsonMessage));
        }
    }

    public void removeSession(Long userId) {
        sessions.remove(userId);
    }

}
