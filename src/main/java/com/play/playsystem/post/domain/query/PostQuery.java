package com.play.playsystem.post.domain.query;

import com.play.playsystem.basic.utils.dto.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostQuery extends PageQuery {
    /**
     * 模糊搜索关键字
     */
    private String keywords;

    /**
     * 排序字段
     */
    private String sortField = "post_created_date";

    /**
     * 排序方式
     */
    private String sortOrder = "desc";

    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 收藏夹id
     */
    private Long favoriteId;
}
