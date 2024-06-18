package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_post_favorite")
public class UserPostFavorite {
    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收藏夹id
     */
    private Long favoriteId;

    /**
     * 收藏时间
     */
    private LocalDateTime createdDate;
}
