package com.play.playsystem.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.post.domain.entity.Favorite;
import com.play.playsystem.post.domain.vo.FavoriteVo;

import java.util.List;

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

    /**
     * 根据用户id获取收藏夹列表
     * @param userId 用户id
     * @return 收藏夹列表
     */
    List<FavoriteVo> getFavoritesByUserId(Long userId);

    /**
     * 获取收藏夹详情
     * @param favoriteId 收藏夹id
     * @param userId 用户id
     */
    JsonResult getFavoriteDetails(Long favoriteId, Long userId);

    /**
     * 检查当前用户是否有该收藏夹权限
     * @param favoriteId 收藏夹id
     * @param userId 用户id
     * @return 是否有权限
     */
    Boolean checkFavorite(Long favoriteId, Long userId);

    /**
     * 根据用户id获取公开的收藏夹
     * @param userId 用户id
     */
    List<FavoriteVo> getOpenedFavoritesByUserId(Long userId);

    /**
     * 由 List<Favorite> 转 List<FavoriteVo>
     * @param favorites Favorite 列表
     * @return FavoriteVo 列表
     */
    List<FavoriteVo> toFavoriteVoList(List<Favorite> favorites);
}
