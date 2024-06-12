package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_post_block")
public class UserPostBlock {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 拉黑时间
     */
    private LocalDateTime createdDate;
}
