package com.play.playsystem.relation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.relation.domain.entity.UserUserFollow;
import com.play.playsystem.relation.mapper.UserUserFollowMapper;
import com.play.playsystem.relation.service.IRelationService;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RelationServiceImpl implements IRelationService {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserUserFollowMapper userUserFollowMapper;

    @Override
    public JsonResult follow(Long followedUserId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (followedUserId == null || userService.UserNotExist(followedUserId)) {
            return jsonResult.setCode(ResultCode.USER_NOT_EXIST).setSuccess(false).setMessage("用户不存在");
        }

        // 查询是否已关注
        QueryWrapper<UserUserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserUserFollow::getFollowedUserId, followedUserId).eq(UserUserFollow::getFansUserId, userId);
        if (userUserFollowMapper.selectOne(queryWrapper) == null) {
            // 未关注
            UserUserFollow userUserFollow = new UserUserFollow(followedUserId, userId, LocalDateTime.now());
            if (userUserFollowMapper.insert(userUserFollow) > 0) {
                return jsonResult;
            }
            throw new RuntimeException("关注异常");
        } else {
            // 已关注
            if (userUserFollowMapper.delete(queryWrapper) > 0) {
                return jsonResult;
            }
            throw new RuntimeException("取消关注异常");
        }
    }
}
