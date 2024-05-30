package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.play.playsystem.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 帖子id
     */
    private Long commentPostId;

    /**
     * 主评论id
     */
    private Long commentMainId;

    /**
     * 回复评论id
     */
    private Long commentReplyId;

    /**
     * 点赞数
     */
    private Integer commentLikesNum;

    /**
     * 拉黑数
     */
    private Integer commentBlackNum;

    /**
     * 创建人id
     */
    private Long commentCreatedId;

    /**
     * 创建人
     */
    @TableField(exist = false)
    private User commentCreatedBy;

    /**
     * 创建时间
     */
    private Date commentCreatedDate;

    /**
     * 子评论数量
     */
    @TableField(exist = false)
    private Integer commentSubCount;

    /**
     * 当前用户是否点赞
     */
    @TableField(exist = false)
    private boolean userLiked = false;
}
