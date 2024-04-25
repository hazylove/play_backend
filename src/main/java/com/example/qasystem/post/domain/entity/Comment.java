package com.example.qasystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // id

    private String commentContent; // 评论内容

    private Long commentPostId; // 帖子id

    private Long commentMainId; // 主评论id

    private Long commentReplyId; // 回复评论id

    private Integer commentLikesNum; // 点赞数

    private Integer commentBlackNum; // 拉黑数

    private Long commentCreatedId; // 创建人id

    @TableField(exist = false)
    private UserBase commentCreatedBy; // 创建人

    private Date commentCreatedDate; // 创建时间

    @TableField(exist = false)
    private Integer commentSubCount; // 子评论数量
}
