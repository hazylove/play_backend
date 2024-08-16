package com.play.playsystem.relation.controller;

import com.play.playsystem.basic.constant.FriendRequestStatusEnum;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.relation.domain.dto.FriendApplicationDto;
import com.play.playsystem.relation.domain.query.RelationQuery;
import com.play.playsystem.relation.service.IRelationService;
import com.play.playsystem.user.utils.UserCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/relation")
@Slf4j
public class RelationController {
    @Autowired
    private IRelationService relationService;

    /**
     * 关注、取消关注
     * @param followedUserId 关注用户id
     */
    @PostMapping("/follow/{followedUserId}")
    public JsonResult follow(@PathVariable("followedUserId") Long followedUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return relationService.follow(followedUserId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 关注列表
     * @param relationQuery 查询参数
     */
    @PostMapping("/followList")
    public JsonResult getFollowList(@RequestBody RelationQuery relationQuery) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            if (relationQuery.getUserId() == null) {
                // 查看本人关注列表
                Long userId = Long.valueOf(authentication.getName());
                relationQuery.setUserId(userId);
            }
            return relationService.getFollowList(relationQuery);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 查看粉丝列表
     * @param relationQuery 查询参数
     */
    @PostMapping("/fansList")
    public JsonResult getFansList(@RequestBody RelationQuery relationQuery) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            if (relationQuery.getUserId() == null) {
                // 查看本人粉丝列表
                Long userId = Long.valueOf(authentication.getName());
                relationQuery.setUserId(userId);
            }
            return relationService.getFansList(relationQuery);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 拉黑用户
     * @param blockedUserId 拉黑用户id
     */
    @PostMapping("/block/{blockedUserId}")
    public JsonResult block(@PathVariable("blockedUserId") Long blockedUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return relationService.block(blockedUserId, userId);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 查看本人拉黑列表
     * @param relationQuery 查询参数
     */
    @PostMapping("/blockList")
    public JsonResult getBlockList(@RequestBody RelationQuery relationQuery) {
        if (relationQuery.getUserId() != null) {
            return new JsonResult().setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("用户异常操作！");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            // 只能查看本人拉黑列表
            Long userId = Long.valueOf(authentication.getName());
            relationQuery.setUserId(userId);
            return relationService.getBlockList(relationQuery);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 添加好友
     * @param friendApplicationDto 申请信息
     */
    @PostMapping("/addFriend")
    public JsonResult addFriend(@RequestBody FriendApplicationDto friendApplicationDto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            friendApplicationDto.setUserId(userId);
            return relationService.addFriend(friendApplicationDto);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 获取好友申请列表
     * @param relationQuery 查询参数
     */
    @PostMapping("/friendApplicationList")
    public JsonResult getFriendApplicationList(@RequestBody RelationQuery relationQuery) {
        if (relationQuery.getUserId() != null) {
            return new JsonResult().setCode(ResultCode.USER_OPERATION_ERROR).setSuccess(false).setMessage("用户异常操作！");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            // 只能查看本人申请列表
            Long userId = Long.valueOf(authentication.getName());
            relationQuery.setUserId(userId);
            return relationService.getFriendApplicationList(relationQuery);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 同意好友申请
     * @param friendApplicationId 好友申请记录id
     */
    @PostMapping("/agreeFriend/{friendApplicationId}")
    public JsonResult agreeFriend(@PathVariable("friendApplicationId") Long friendApplicationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return relationService.approveFriendApplication(friendApplicationId, userId, FriendRequestStatusEnum.ACCEPTED);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }

    /**
     * 拒绝好友申请
     * @param friendApplicationId 申请记录id
     */
    @PostMapping("/rejectFriend/{friendApplicationId}")
    public JsonResult rejectFriend(@PathVariable("friendApplicationId") Long friendApplicationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserCheckUtil.checkAuth(authentication)) {
            Long userId = Long.valueOf(authentication.getName());
            return relationService.approveFriendApplication(friendApplicationId, userId, FriendRequestStatusEnum.REJECTED);
        }
        return new JsonResult().setCode(ResultCode.FORBIDDEN_CODE).setSuccess(false).setMessage("未认证用户！");
    }
}
