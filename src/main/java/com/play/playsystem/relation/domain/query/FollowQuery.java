package com.play.playsystem.relation.domain.query;

import com.play.playsystem.basic.utils.dto.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FollowQuery extends PageQuery {
    private String keywords;
    private Long userId;
}
