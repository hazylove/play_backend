package com.play.playsystem.relation.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_user_block")
public class UserUserBlock {
    /**
     * 拉黑用户id
     */
    private Long blockedUserId;

    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 拉黑时间
     */
    private LocalDateTime createdDate;
}
