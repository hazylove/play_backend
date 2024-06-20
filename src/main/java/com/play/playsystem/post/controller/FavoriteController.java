package com.play.playsystem.post.controller;

import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.post.domain.entity.Favorite;
import com.play.playsystem.post.service.IFavoriteService;
import com.play.playsystem.user.utils.UserCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/favorite")
@Slf4j
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;

    /**
     * 新增收藏夹
     * @param favorite 收藏夹信息
     */
    @PostMapping("/add")
    public JsonResult addFavorite(@RequestBody Favorite favorite) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            favorite.setCreatedId(userId);
            favoriteService.insert(favorite);
            return new JsonResult();
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 修改收藏夹信息
     * @param favorite 收藏夹信息
     */
    @PostMapping("/update")
    public JsonResult updateFavorite(@RequestBody Favorite favorite) {
        if (favorite.getId() == null) {
            return new JsonResult().setCode(ResultCode.UNPROCESSABLE_ENTITY).setSuccess(false).setMessage("缺少参数：id");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            favorite.setCreatedId(userId);
            return favoriteService.updateFavorite(favorite, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 删除收藏夹
     * @param favoriteId 收藏夹id
     */
    @DeleteMapping("/{favoriteId}")
    public JsonResult deleteFavorite(@PathVariable Long favoriteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return favoriteService.deleteFavorite(favoriteId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 获取本人用户收藏夹
     */
    @GetMapping("/personalList")
    public JsonResult getPersonalFavorites() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
            return new JsonResult().setData(favorites);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 获取收藏夹详情
     * @param favoriteId 收藏夹id
     */
    @GetMapping("/details/{favoriteId}")
    public JsonResult getFavorite(@PathVariable Long favoriteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return favoriteService.getFavoriteDetails(favoriteId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }
}
