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
public class PostVo {
    /**
     * id
     */
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
     * 创建人id
     */
    private Long postCreatedId;

    /**
     * 创建人
     */
    private UserCreatedVo postCreatedBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postCreatedDate;

    /**
     * 评论数量
     */
    private int postCommentNum;

    /**
     * 当前用户是否点赞
     */
    private boolean userLiked;
}
