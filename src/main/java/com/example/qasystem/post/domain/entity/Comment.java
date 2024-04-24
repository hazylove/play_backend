package com.example.qasystem.post.domain.entity;

import com.example.qasystem.user.domain.entity.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Comment")
public class Comment {
    // id
    private Long id;
    // 评论内容
    private String commentContent;
    // 帖子id
    private Long commentPostId;
    // 主评论id
    private Long commentMainId;
    // 回复评论id
    private Long commentReplyId;
    // 点赞数
    private Integer commentLikesNum;
    // 拉黑数
    private Integer commentBlackNum;
    // 创建人id
    private Long commentCreatedId;
    // 创建人
    private UserBase commentCreatedBy;
    // 创建时间
    private Date commentCreatedDate;
    // 子评论数量
    private Integer commentSubCount;
}
