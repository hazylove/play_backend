package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_post_likes")
public class UserPostLikes {

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 用户id
     */
    private Long userId;
}
