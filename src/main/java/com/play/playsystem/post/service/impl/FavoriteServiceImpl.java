package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Favorite;
import com.play.playsystem.post.mapper.FavoriteMapper;
import com.play.playsystem.post.service.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Favorite favorite) {
        favorite.setCreatedDate(LocalDateTime.now());
        favorite.setUpdateDate(LocalDateTime.now());
        if (favoriteMapper.insert(favorite) < 0) {
            throw new RuntimeException("新建收藏夹操作异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult updateFavorite(Favorite favorite, Long userId) {
        JsonResult jsonResult = new JsonResult();
        // 查询创建人
        Long createdId = favoriteMapper.getCreatedIdById(favorite.getId());
        // 创建人与当前用户不同，拒绝修改
        if (!Objects.equals(createdId, userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("异常修改操作");
        }
        favorite.setUpdateDate(LocalDateTime.now());
        if (!updateById(favorite)) {
            throw new RuntimeException("修改收藏夹操作异常");
        }
        return new JsonResult();
    }

    @Override
    public JsonResult deleteFavorite(Long favoriteId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        // 查询创建人
        Long createdId = favoriteMapper.getCreatedIdById(favoriteId);
        // 创建人与当前用户不同，拒绝删除
        if (!Objects.equals(createdId, userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("异常删除操作");
        }
        if (favoriteMapper.deleteById(favoriteId) < 0) {
            throw new RuntimeException("删除收藏夹操作异常");
        }
        return jsonResult;
    }
}
