package com.example.qasystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_post")
public class Post {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // id

    private String postTitle; // 标题

    private String postContent; // 内容

    private String postTag; // 标签

    private Long postCreatedId; // 创建人id

    @TableField(exist = false)
    private UserBase postCreatedBy; // 创建人

    private Date postCreatedDate; // 创建时间

    @TableField(exist = false)
    private int postCommentNum; // 评论数量
}
