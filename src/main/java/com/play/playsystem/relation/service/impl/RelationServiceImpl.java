package com.play.playsystem.relation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.play.playsystem.basic.constant.MessageTypeEnum;
import com.play.playsystem.basic.handler.MyWebSocketHandler;
import com.play.playsystem.basic.utils.dto.PageList;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.MessageResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.basic.constant.FriendRequestStatusEnum;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.relation.domain.dto.FriendApplicationDto;
import com.play.playsystem.relation.domain.entity.FriendApplication;
import com.play.playsystem.relation.domain.entity.UserUserBlock;
import com.play.playsystem.relation.domain.entity.UserUserFollow;
import com.play.playsystem.relation.domain.entity.UserUserFriend;
import com.play.playsystem.relation.domain.query.RelationQuery;
import com.play.playsystem.relation.domain.vo.FriendApplicationVo;
import com.play.playsystem.relation.mapper.FriendApplicationMapper;
import com.play.playsystem.relation.mapper.UserUserBlockMapper;
import com.play.playsystem.relation.mapper.UserUserFollowMapper;
import com.play.playsystem.relation.mapper.UserUserFriendMapper;
import com.play.playsystem.relation.service.IRelationService;
import com.play.playsystem.user.domain.vo.UserListVo;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RelationServiceImpl implements IRelationService {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserUserFollowMapper userUserFollowMapper;

    @Autowired
    private UserUserBlockMapper userUserBlockMapper;

    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @Autowired
    private FriendApplicationMapper friendApplicationMapper;

    @Autowired
    private UserUserFriendMapper userUserFriendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            return jsonResult.setCode(ResultCode.BLOCKED_NOT_OPERATE).setSuccess(false).setMessage("被对方拉黑，不可关注");
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
    public JsonResult getFollowList(RelationQuery relationQuery) {
        Long total = userUserFollowMapper.countFollow(relationQuery);
        List<UserListVo> userList = userUserFollowMapper.getFollowList(relationQuery);
        userList.forEach(user -> user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar())));
        return new JsonResult().setData(new PageList<>(total, userList));
    }

    @Override
    public JsonResult getFansList(RelationQuery relationQuery) {
        Long total = userUserFollowMapper.countFans(relationQuery);
        List<UserListVo> userList = userUserFollowMapper.getFansList(relationQuery);
        userList.forEach(user -> user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar())));
        return new JsonResult().setData(new PageList<>(total, userList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public JsonResult getBlockList(RelationQuery relationQuery) {
        Long total = userUserBlockMapper.countBlock(relationQuery);
        List<UserListVo> userList = userUserBlockMapper.getBlockList(relationQuery);
        userList.forEach(user -> user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar())));
        return new JsonResult().setData(new PageList<>(total, userList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult addFriend(FriendApplicationDto friendApplicationDto) throws IOException {
        JsonResult jsonResult = new JsonResult();

        Long friendId = friendApplicationDto.getApplyUserId();
        Long userId = friendApplicationDto.getUserId();

        if (friendId == null || userId == null) {
            return jsonResult.setCode(ResultCode.UNPROCESSABLE_ENTITY).setSuccess(false).setMessage("参数错误");
        }

        if (friendId.equals(userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("不可想自己发送申请");
        }

        // 查询是否被拉黑
        QueryWrapper<UserUserBlock> blockQueryWrapper = new QueryWrapper<>();
        blockQueryWrapper.lambda().eq(UserUserBlock::getBlockedUserId, userId).eq(UserUserBlock::getUserId, friendId);
        if (userUserBlockMapper.selectOne(blockQueryWrapper) != null) {
            return jsonResult.setCode(ResultCode.BLOCKED_NOT_OPERATE).setSuccess(false).setMessage("被对方拉黑，不可添加");
        }

        // 查询是否已有好友申请
        QueryWrapper<FriendApplication> applicationQueryWrapper = new QueryWrapper<>();
        applicationQueryWrapper.lambda().eq(FriendApplication::getUserId, userId).eq(FriendApplication::getApplyUserId, friendId);
        FriendApplication friendApplication = friendApplicationMapper.selectOne(applicationQueryWrapper);

        if (friendApplication == null) {
            // 保存好友请求
            friendApplication = new FriendApplication(
                    null,
                    userId,
                    friendId,
                    friendApplicationDto.getApplyInfo(),
                    false,
                    FriendRequestStatusEnum.PENDING,
                    LocalDateTime.now()
            );
            if (friendApplicationMapper.insert(friendApplication) > 0) {
                sendFriendApplicationMessage(friendId, userId, friendApplication.getId());
            }
        } else {
            if (friendApplication.getBeRead()) {
                friendApplication.setStatus(FriendRequestStatusEnum.PENDING);
                friendApplication.setBeRead(false);
                if (friendApplicationMapper.updateById(friendApplication) > 0) {
                    sendFriendApplicationMessage(friendId, userId, friendApplication.getId());
                }
            }
        }

        return jsonResult;
    }

    @Override
    public JsonResult getFriendApplicationList(RelationQuery relationQuery) {
        Long total = friendApplicationMapper.countFriendApplication(relationQuery);
        List<FriendApplicationVo> userList = friendApplicationMapper.getFriendApplicationList(relationQuery);
        userList.forEach(user -> user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar())));
        return new JsonResult().setData(new PageList<>(total, userList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult approveFriendApplication(Long friendApplicationId, Long userId, FriendRequestStatusEnum friendRequestStatus) {
        JsonResult jsonResult = new JsonResult();
        FriendApplication friendApplication = friendApplicationMapper.selectById(friendApplicationId);
        if (friendApplication == null) {
            return jsonResult.setCode(ResultCode.DATA_NOT_EXIST).setSuccess(false).setMessage("申请已取消");
        }
        if (friendApplication.getStatus() != FriendRequestStatusEnum.PENDING) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("数据异常，请刷新重试");
        }
        if ((friendRequestStatus == FriendRequestStatusEnum.ACCEPTED || friendRequestStatus == FriendRequestStatusEnum.REJECTED) && !Objects.equals(friendApplication.getApplyUserId(), userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("用户异常操作");
        }

        friendApplication.setStatus(friendRequestStatus);
        friendApplicationMapper.updateById(friendApplication);
        UserUserFriend userUserFriend1 = new UserUserFriend(null, friendApplication.getUserId(), userId, LocalDateTime.now());
        UserUserFriend userUserFriend2 = new UserUserFriend(null, userId, friendApplication.getUserId(), LocalDateTime.now());
        userUserFriendMapper.insert(userUserFriend1);
        userUserFriendMapper.insert(userUserFriend2);

        return jsonResult;
    }

    @Override
    public JsonResult deleteFriendApplication(Long friendApplicationId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        FriendApplication friendApplication = friendApplicationMapper.selectById(friendApplicationId);
        if (!Objects.equals(friendApplication.getApplyUserId(), userId)) {
            return jsonResult.setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("用户异常操作");
        }
        friendApplicationMapper.deleteById(friendApplicationId);
        return jsonResult;
    }

    private void sendFriendApplicationMessage(Long friendId, Long userId, Long friendApplicationId) throws IOException {
        if (userService.isUserOnline(friendId)) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("requestId", friendApplicationId);
            dataMap.put("fromUserId", userId);
            dataMap.put("message", "你有新的好友申请！");
            dataMap.put("timestamp", LocalDateTime.now().toString());
            MessageResult messageResult = new MessageResult(MessageTypeEnum.FRIEND_APPLICATION, dataMap);
            // 立即通知在线用户
            webSocketHandler.sendMessageToUser(friendId, messageResult);
        }
    }
}
