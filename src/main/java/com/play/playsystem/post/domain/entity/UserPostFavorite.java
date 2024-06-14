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
    private Long post_id;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 收藏夹id
     */
    private Long favorite_id;

    /**
     * 收藏时间
     */
    private LocalDateTime createdDate;
}
