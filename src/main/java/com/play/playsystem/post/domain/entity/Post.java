package com.play.playsystem.post.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.play.playsystem.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
     * i标题
     */
    private String postTitle;

    /**
     * 内容
     */
    private String postContent;

    /**
     * 标签
     */
    private String postTag;

    /**
     * 点赞数
     */
    private int postLikesNum;

    /**
     * 创建人id
     */
    private Long postCreatedId;

    @TableField(exist = false)
    private User postCreatedBy; // 创建人

    private Date postCreatedDate; // 创建时间

    @TableField(exist = false)
    private int postCommentNum; // 评论数量
}
