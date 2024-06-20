package com.play.playsystem.post.domain.vo;

import com.play.playsystem.post.domain.entity.Favorite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteVo {
    /**
     * id
     */
    private Long id;

    /**
     * 收藏夹名称
     */
    private String favoriteName;

    /**
     * 所属用户id
     */
    private Long createdId;

    /**
     * 描述
     */
    private String introduction;

    /**
     * 帖子数量
     */
    private int postNum;

    /**
     * 是否公开
     */
    private boolean opened;

    /**
     * 最新更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;

    /**
     * 根据 Favorite 实体构造 FavoriteVo 对象
     * @param favorite Favorite实体
     */
    public FavoriteVo(Favorite favorite) {
        if (favorite != null) {
            this.id = favorite.getId();
            this.favoriteName = favorite.getFavoriteName();
            this.createdId = favorite.getCreatedId();
            this.introduction = favorite.getIntroduction();
            this.postNum = favorite.getPostNum();
            this.opened = favorite.isOpened();
            this.updateDate = favorite.getUpdateDate();
            this.createdDate = favorite.getCreatedDate();
        }
    }
}
