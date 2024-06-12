package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Integer commentBlocksNum;

    /**
     * 创建人id
     */
    private Long commentCreatedId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentCreatedDate;
}
