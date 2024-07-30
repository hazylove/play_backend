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
public class FavoriteQuery extends PageQuery {
    private Long userId;
    private boolean owner = true;
}
