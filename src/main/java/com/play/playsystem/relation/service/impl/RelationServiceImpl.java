package com.play.playsystem.relation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.relation.domain.entity.UserUserBlock;
import com.play.playsystem.relation.domain.entity.UserUserFollow;
import com.play.playsystem.relation.domain.query.FollowQuery;
import com.play.playsystem.relation.mapper.UserUserBlockMapper;
import com.play.playsystem.relation.mapper.UserUserFollowMapper;
import com.play.playsystem.relation.service.IRelationService;
import com.play.playsystem.user.domain.vo.UserListVo;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelationServiceImpl implements IRelationService {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserUserFollowMapper userUserFollowMapper;

    @Autowired
    private UserUserBlockMapper userUserBlockMapper;

    @Override
    public JsonResult follow(Long followedUserId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (followedUserId == null || userService.UserNotExist(followedUserId)) {
            return jsonResult.setCode(ResultCode.USER_NOT_EXIST).setSuccess(false).setMessage("用户不存在");
        }

        if (followedUserId.equals(userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("不可关注自己");
        }

        // 查询是否被拉黑
        QueryWrapper<UserUserBlock> blockQueryWrapper = new QueryWrapper<>();
        blockQueryWrapper.lambda().eq(UserUserBlock::getBlockedUserId, userId).eq(UserUserBlock::getUserId, followedUserId);
        if (userUserBlockMapper.selectOne(blockQueryWrapper) != null) {
            return jsonResult.setCode(ResultCode.CANNOT_FOLLOW).setSuccess(false).setMessage("被对方拉黑，不可关注");
        }

        // 查询是否已关注
        QueryWrapper<UserUserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserUserFollow::getFollowedUserId, followedUserId).eq(UserUserFollow::getFansUserId, userId);
        if (userUserFollowMapper.selectOne(queryWrapper) == null) {
            // 未关注
            UserUserFollow userUserFollow = new UserUserFollow(followedUserId, userId, LocalDateTime.now());
            if (userUserFollowMapper.insert(userUserFollow) > 0) {
                // 取消拉黑对方
                QueryWrapper<UserUserBlock> blockDeleteQueryWrapper = new QueryWrapper<>();
                blockDeleteQueryWrapper.lambda().eq(UserUserBlock::getBlockedUserId, followedUserId).eq(UserUserBlock::getUserId, userId);
                userUserBlockMapper.delete(blockDeleteQueryWrapper);
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

    @Override
    public JsonResult getFollowList(FollowQuery followQuery) {
        Long total = userUserFollowMapper.countFollow(followQuery);
        List<UserListVo> userList = userUserFollowMapper.getFollowList(followQuery);
        userList.forEach(user -> user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar())));
        return new JsonResult().setData(new PageList<>(total, userList));
    }

    @Override
    public JsonResult getFansList(FollowQuery followQuery) {
        Long total = userUserFollowMapper.countFans(followQuery);
        List<UserListVo> userList = userUserFollowMapper.getFansList(followQuery);
        userList.forEach(user -> user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar())));
        return new JsonResult().setData(new PageList<>(total, userList));
    }

    @Override
    public JsonResult block(Long blockedUserId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        if (blockedUserId == null || userService.UserNotExist(blockedUserId)) {
            return jsonResult.setCode(ResultCode.USER_NOT_EXIST).setSuccess(false).setMessage("用户不存在");
        }

        if (blockedUserId.equals(userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("不可拉黑自己");
        }

        // 查询是否已关注
        QueryWrapper<UserUserBlock> blockQueryWrapper = new QueryWrapper<>();
        blockQueryWrapper.lambda().eq(UserUserBlock::getBlockedUserId, blockedUserId).eq(UserUserBlock::getUserId, userId);
        if (userUserBlockMapper.selectOne(blockQueryWrapper) == null) {
            // 未拉黑
            UserUserBlock userUserBlock = new UserUserBlock(blockedUserId, userId, LocalDateTime.now());
            // 拉黑操作
            if (userUserBlockMapper.insert(userUserBlock) > 0) {
                // 取消互相关注
                QueryWrapper<UserUserFollow> followQueryWrapper = new QueryWrapper<>();
                followQueryWrapper.lambda()
                        .and(wrapper -> wrapper.eq(UserUserFollow::getFollowedUserId, blockedUserId).eq(UserUserFollow::getFansUserId, userId))
                        .or(wrapper -> wrapper.eq(UserUserFollow::getFollowedUserId, userId).eq(UserUserFollow::getFansUserId, blockedUserId));
                userUserFollowMapper.delete(followQueryWrapper);
                return jsonResult;
            }
            throw new RuntimeException("拉黑异常");
        } else {
            // 已关注
            if (userUserBlockMapper.delete(blockQueryWrapper) > 0) {
                return jsonResult;
            }
            throw new RuntimeException("取消拉黑异常");
        }
    }
}