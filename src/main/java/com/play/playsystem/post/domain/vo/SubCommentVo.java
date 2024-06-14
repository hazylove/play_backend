package com.play.playsystem.post.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.play.playsystem.user.domain.vo.UserCreatedVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCommentVo {
    /**
     * id
     */
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
     * 回复用户
     */
    private UserCreatedVo commentReply;

    /**
     * 点赞数
     */
    private Integer commentLikesNum;

    /**
     * 拉黑数
     */
    private Integer commentBlockNum;

    /**
     * 创建人id
     */
    private Long commentCreatedId;

    /**
     * 创建人
     */
    private UserCreatedVo commentCreatedBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentCreatedDate;

    /**
     * 当前用户是否点赞
     */
    private boolean userLiked = false;

    /**
     * 当前用户是否拉黑
     */
    private boolean userBlocked = false;
}
