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
public class MainCommentVo {
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
    private UserCreatedVo commentCreatedBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentCreatedDate;

    /**
     * 子评论数量
     */
    private Integer commentSubCount;

    /**
     * 当前用户是否点赞
     */
    private boolean userLiked = false;
}
