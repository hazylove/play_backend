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
    private String keywords;
    private String sortField = "post_created_date";
    private String sortOrder = "desc";
}