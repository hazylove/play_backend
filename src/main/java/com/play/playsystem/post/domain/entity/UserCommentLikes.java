package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_comment_likes")
public class UserCommentLikes {
    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 用户id
     */
    private Long userId;
}
