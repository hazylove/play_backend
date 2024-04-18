package com.example.qasystem.post.domain.query;

import com.example.qasystem.basic.utils.dto.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentQuery extends PageQuery {
    private Long postId;
    private String sortField = "comment_created_date";
    private String sortOrder = "desc";
    private Boolean isMain;
    private Long mainId;
}
