package com.play.playsystem.post.domain.query;

import com.play.playsystem.basic.utils.dto.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentQuery extends PageQuery {
    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 排序字段
     */
    private String sortField = "comment_created_date";

    /**
     * 排序方式
     */
    private String sortOrder = "desc";

    /**
     * 是否主评论
     */
    private Boolean isMain;

    /**
     * 主评论id
     */
    private Long mainId;

    /**
     * 当前用户id
     */
    private Long userId;
}
