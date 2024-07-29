package com.play.playsystem.relation.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_user_follow")
public class UserUserFollow {
    /**
     * 被关注用户id
     */
    private Long followedUserId;

    /**
     * 关注用户id
     */
    private Long fansUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;
}
