package com.play.playsystem.basic.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public abstract class MyAbstractWebSocketHandler extends TextWebSocketHandler {
    // 会话列表
    protected final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 心跳检测间隔
    @Value("${websocket.heartbeat-interval}")
    private long heartbeatInterval;

    // 定时任务
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // 定时任务列表
    private final Map<Long, ScheduledFuture<?>> heartbeatFutures = new ConcurrentHashMap<>();
    // 最后一次接收pang时间列表
    private final Map<Long, Long> lastPongTimestamps = new ConcurrentHashMap<>();

    // 连接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String token = extractToken(session);
        Long userId = extractUserIdFromToken(token);
        if (userId != null) {
            session.getAttributes().put("userId", userId);
            sessions.put(userId, session);
            startHeartbeat(userId, session);
        } else {
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭WebSocket会话异常: ", e);
            }
        }
    }

    // 启动心跳检测
    private void startHeartbeat(Long userId, WebSocketSession session) {
        // 发送ping
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new PingMessage());
                } else {
                    removeSession(userId);
                    log.warn("WebSocket会话已关闭，无法发送Ping消息");
                }
            } catch (IOException e) {
                log.error("发送Ping消息失败: ", e);
            }
        }, heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);

        heartbeatFutures.put(userId, future);

        // 检测pang
        scheduler.scheduleAtFixedRate(() -> {
            Long lastPongTimestamp = lastPongTimestamps.get(userId);
            if (lastPongTimestamp == null || (System.currentTimeMillis() - lastPongTimestamp > heartbeatInterval * 3)) {
                if (session.isOpen()) {
                    try {
                        session.close(CloseStatus.GOING_AWAY.withReason("超时未收到Pong消息"));
                    } catch (IOException e) {
                        log.error("关闭WebSocket会话失败: ", e);
                    }
                }
            }
        }, heartbeatInterval * 3, heartbeatInterval * 3, TimeUnit.MILLISECONDS);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            removeSession(userId);
        }
    }

    @Override
    public void handlePongMessage(WebSocketSession session, PongMessage message) {
        Long userId = (Long) session.getAttributes().get("userId");
        lastPongTimestamps.put(userId, System.currentTimeMillis());
    }

    protected String extractToken(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        int tokenStart = uri.indexOf("token=");
        if (tokenStart == -1) {
            log.error("URI不存在token: {}", uri);
            return "";
        }
        return uri.substring(tokenStart + 6);
    }

    protected abstract Long extractUserIdFromToken(String token);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    public void removeSession(Long userId) {
        // 移除会话
        sessions.remove(userId);
        // 关闭心跳检测
        ScheduledFuture<?> future = heartbeatFutures.remove(userId);
        if (future != null) {
            future.cancel(true);
        }
    }
}