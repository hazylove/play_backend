package com.play.playsystem.relation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_user_friend")
public class UserUserFriend {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户1id
     */
    private Long userId1;

    /**
     * 用户2id
     */
    private Long userId2;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;
}
