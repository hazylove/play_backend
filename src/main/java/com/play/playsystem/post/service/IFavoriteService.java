package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.post.domain.entity.Favorite;

public interface IFavoriteService extends IService<Favorite> {
    /**
     * 新增收藏夹
     * @param favorite 收藏夹对象
     */
    void insert(Favorite favorite);

    /**
     * 修改收藏夹
     * @param favorite 收藏夹对象
     * @param userId 当前用户id
     */
    JsonResult updateFavorite(Favorite favorite, Long userId);

    /**
     * 删除收藏夹
     * @param favoriteId 收藏夹id
     * @param userId 当前用户id
     */
    JsonResult deleteFavorite(Long favoriteId, Long userId);
}
