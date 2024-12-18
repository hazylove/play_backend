package com.play.playsystem.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Favorite;
import com.play.playsystem.post.domain.entity.UserPostFavorite;
import com.play.playsystem.post.domain.query.FavoriteQuery;
import com.play.playsystem.post.domain.vo.FavoriteVo;
import com.play.playsystem.post.mapper.FavoriteMapper;
import com.play.playsystem.post.mapper.UserPostFavoriteMapper;
import com.play.playsystem.post.service.IFavoriteService;
import com.play.playsystem.user.domain.vo.UserCreatedVo;
import com.play.playsystem.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserPostFavoriteMapper userPostFavoriteMapper;

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

    @Override
    public PageList<FavoriteVo> getFavoritesByUserId(FavoriteQuery favoriteQuery) {
        Long total = favoriteMapper.countFavoritesByUserId(favoriteQuery);
        List<FavoriteVo> favoriteVoList = favoriteMapper.getFavoritesByUserId(favoriteQuery);
        return new PageList<>(total, setFavoriteVoCreator(favoriteVoList));
    }

    @Override
    public JsonResult getFavoriteDetails(Long favoriteId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (!checkFavorite(favoriteId, userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("用户异常操作");
        }
        Favorite favorite = favoriteMapper.selectById(favoriteId);
        FavoriteVo favoriteVo = new FavoriteVo(favorite);
        favoriteVo.setCreatedBy(userService.getUserCreatedVo(favoriteVo.getCreatedId()));
        QueryWrapper<UserPostFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserPostFavorite::getFavoriteId, favoriteId);
        favoriteVo.setPostNum(Math.toIntExact(userPostFavoriteMapper.selectCount(queryWrapper)));
        return jsonResult.setData(favoriteVo);
    }

    @Override
    public Boolean checkFavorite(Long favoriteId, Long userId) {
        Long favoriteCreatedId = favoriteMapper.getCreatedIdById(favoriteId);
        if (!Objects.equals(favoriteCreatedId, userId)) {
            Boolean result = favoriteMapper.getOpenedById(favoriteId);
            return result != null && result;
        }
        return true;
    }

    @Override
    public List<FavoriteVo> toFavoriteVoList(List<Favorite> favorites) {
        if (favorites == null) {
            return null;
        }
        return favorites.stream()
                .map(FavoriteVo::new)
                .collect(Collectors.toList());
    }

    private List<FavoriteVo> setFavoriteVoCreator(List<FavoriteVo> favoriteVoList) {
        Map<Long, UserCreatedVo> userCreatedVoMap = new HashMap<>();
        favoriteVoList.forEach(favoriteVo -> {
            Long userId = favoriteVo.getCreatedId();
            if (!userCreatedVoMap.containsKey(userId)) {
                UserCreatedVo userCreatedVo = userService.getUserCreatedVo(userId);
                userCreatedVoMap.put(userId, userCreatedVo);
            }
            favoriteVo.setCreatedBy(userCreatedVoMap.get(userId));
        });
        return favoriteVoList;
    }
}
