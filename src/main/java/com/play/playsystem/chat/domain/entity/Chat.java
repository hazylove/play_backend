package com.play.playsystem.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.play.playsystem.chat.domain.constant.ChatStatusEnum;
import com.play.playsystem.chat.domain.constant.ChatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chat")
public class Chat {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者id
     */
    private Long senderId;

    /**
     * 接收者id
     */
    private Long receiverId;

    /**
     * 群聊id
     */
    private Long groupId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private ChatTypeEnum chatType;

    /**
     * 消息状态
     */
    private ChatStatusEnum status;

    /**
     * 回复消息id
     */
    private Long replTo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
}
