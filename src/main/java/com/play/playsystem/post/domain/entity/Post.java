package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 帖子实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_post")
public class Post {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String postTitle;

    /**
     * 内容
     */
    private String postContent;

    /**
     * 点赞数
     */
    private int postLikesNum;

    /**
     * 拉黑数
     */
    private int postBlocksNum;

    /**
     * 创建人id
     */
    private Long postCreatedId;


    /**
     * 创建时间
     */
    private LocalDateTime postCreatedDate;

    /**
     * 评论数量
     */
    @TableField(exist = false)
    private int postCommentNum;

    /**
     * 当前用户是否点赞
     */
    @TableField(exist = false)
    private boolean userLiked;
}
