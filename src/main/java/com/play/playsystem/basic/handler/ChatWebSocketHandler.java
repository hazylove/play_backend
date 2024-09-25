package com.play.playsystem.basic.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.play.playsystem.chat.domain.constant.ChatStatusEnum;
import com.play.playsystem.chat.domain.constant.ChatTypeEnum;
import com.play.playsystem.chat.domain.entity.Chat;
import com.play.playsystem.chat.mapper.ChatMapper;
import com.play.playsystem.relation.domain.entity.UserUserBlock;
import com.play.playsystem.relation.mapper.UserUserBlockMapper;
import com.play.playsystem.user.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class ChatWebSocketHandler extends MyAbstractWebSocketHandler {
    @Autowired
    private UserUtil userUtils;

    @Autowired
    private UserUserBlockMapper userUserBlockMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Override
    protected Long extractUserIdFromToken(String token) {
        return userUtils.extractUserIdFromToken(token);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 获取发送消息用户
        String token = extractToken(session);
        Long senderId = extractUserIdFromToken(token);

        // 处理传入消息
        String jsonPayload = message.getPayload();

        // 解析JSON并构建消息对象
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonPayload);

            // 提取消息
            // 接收者id
            Long receiverId = jsonNode.has("receiverId") && !jsonNode.get("receiverId").isNull()
                    ? Long.valueOf(jsonNode.get("receiverId").asText())
                    : null;

            // 群聊id
            Long groupId = jsonNode.has("groupId") && !jsonNode.get("groupId").isNull()
                    ? Long.valueOf(jsonNode.get("groupId").asText())
                    : null;

            // 消息内容
            String content = jsonNode.has("content") && !jsonNode.get("content").isNull()
                    ? jsonNode.get("content").asText()
                    : null;

            // 消息类型
            ChatTypeEnum chatType = jsonNode.has("chatType") && !jsonNode.get("chatType").isNull()
                    ? ChatTypeEnum.fromKey(jsonNode.get("chatType").asText())
                    : null;

            // 回复消息id
            Long replTo = jsonNode.has("replTo") && !jsonNode.get("replTo").isNull()
                    ? Long.valueOf(jsonNode.get("replTo").asText())
                    : null;


            // 构建新的消息对象
            Chat chat = new Chat(
                    null,
                    senderId,
                    receiverId,
                    groupId,
                    content,
                    chatType,
                    ChatStatusEnum.UNREAD,
                    replTo,
                    LocalDateTime.now()
            );

            System.out.println(chat);
            // 判断发给用户 or 发给群聊
            if (receiverId != null) {
                // 判断是否被拉黑
                QueryWrapper<UserUserBlock> blockQueryWrapper = new QueryWrapper<>();
                blockQueryWrapper.lambda().eq(UserUserBlock::getBlockedUserId, senderId).eq(UserUserBlock::getUserId, receiverId);
                if (userUserBlockMapper.selectOne(blockQueryWrapper) != null) {
                    chat.setStatus(ChatStatusEnum.FAILED);
                } else {
                    // 通过token初步判断用户是否在线
                    if (userUtils.isUserOnline(receiverId)) {
                        sendMessageToUser(receiverId, chat);
                    }
                }

            } else if (groupId != null) {
                log.info("发送群聊...");
            }
            System.out.println(chat);
            // 保存消息
            chatMapper.insert(chat);

        } catch (IOException e) {
            log.error("消息处理失败", e);
        }
    }

    public void sendMessageToUser(Long userId, Chat chat) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonMessage = objectMapper.writeValueAsString(chat);
            session.sendMessage(new TextMessage(jsonMessage));
        }
    }

}
